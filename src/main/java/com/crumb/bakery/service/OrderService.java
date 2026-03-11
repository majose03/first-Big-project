package com.crumb.bakery.service;

import com.crumb.bakery.model.Order;
import com.crumb.bakery.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repo;

    @Autowired
    private BakeryService bakeryService;

    private static final Set<String> VALID = Set.of(
            "pending", "confirmed", "preparing", "out-for-delivery", "delivered", "cancelled");

    // ==========================================
    // ADVANCED DSA: Custom Thread-Safe LRU Cache
    // O(1) Time Complexity for Lookups & Evictions
    // ==========================================
    private static class LruNode {
        Long key;
        Order value;
        LruNode prev, next;

        LruNode(Long key, Order value) {
            this.key = key;
            this.value = value;
        }
    }

    private static class OrderLruCache {
        private final int capacity;
        private final Map<Long, LruNode> cache = new HashMap<>();
        private final LruNode head = new LruNode(null, null);
        private final LruNode tail = new LruNode(null, null);
        private final ReentrantLock lock = new ReentrantLock();

        public OrderLruCache(int capacity) {
            this.capacity = capacity;
            head.next = tail;
            tail.prev = head;
        }

        public void put(Long key, Order value) {
            lock.lock();
            try {
                if (cache.containsKey(key)) {
                    LruNode node = cache.get(key);
                    node.value = value;
                    removeNode(node);
                    addToHead(node);
                } else {
                    LruNode node = new LruNode(key, value);
                    cache.put(key, node);
                    addToHead(node);
                    if (cache.size() > capacity) {
                        LruNode lru = tail.prev;
                        removeNode(lru);
                        cache.remove(lru.key);
                    }
                }
            } finally {
                lock.unlock();
            }
        }

        public Order get(Long key) {
            lock.lock();
            try {
                if (cache.containsKey(key)) {
                    LruNode node = cache.get(key);
                    removeNode(node);
                    addToHead(node);
                    return node.value;
                }
                return null;
            } finally {
                lock.unlock();
            }
        }

        public void remove(Long key) {
            lock.lock();
            try {
                if (cache.containsKey(key)) {
                    removeNode(cache.remove(key));
                }
            } finally {
                lock.unlock();
            }
        }

        private void removeNode(LruNode node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        private void addToHead(LruNode node) {
            node.next = head.next;
            node.next.prev = node;
            node.prev = head;
            head.next = node;
        }
    }

    private final OrderLruCache orderCache = new OrderLruCache(100);

    public Order createOrder(Order order) {
        order.setStatus("pending");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        Order saved = bakeryService.processNewOrder(order);
        orderCache.put(saved.getId(), saved); // Add to LRU
        return saved;
    }

    public List<Order> getAllOrders() {
        return repo.findByOrderByCreatedAtDesc();
    }

    public Optional<Order> getOrderById(Long id) {
        // O(1) LRU Cache lookup
        Order cached = orderCache.get(id);
        if (cached != null) {
            return Optional.of(cached);
        }
        Optional<Order> dbOrder = repo.findById(id);
        dbOrder.ifPresent(order -> orderCache.put(id, order));
        return dbOrder;
    }

    public List<Order> getOrdersByEmail(String email) {
        return repo.findByEmail(email);
    }

    public List<Order> getOrdersByStatus(String status) {
        return repo.findByStatus(status);
    }

    public Order updateStatus(Long id, String status) {
        if (!VALID.contains(status.toLowerCase()))
            throw new IllegalArgumentException("Invalid status: " + status);

        Order o = getOrderById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
        o.setStatus(status.toLowerCase());
        o.setUpdatedAt(LocalDateTime.now());

        Order saved = repo.save(o);
        orderCache.put(id, saved); // Update LRU Cache
        return saved;
    }

    public void deleteOrder(Long id) {
        repo.deleteById(id);
        orderCache.remove(id); // Remove from LRU Cache
    }

    public Map<String, Long> getStatusSummary() {
        Map<String, Long> m = new LinkedHashMap<>();
        for (String s : VALID)
            m.put(s, repo.countByStatus(s));
        return m;
    }

    public long getTotalOrderCount() {
        return repo.count();
    }
}
