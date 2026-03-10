package com.crumb.bakery.service;

import com.crumb.bakery.model.Product;
import com.crumb.bakery.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repo;

    // ==========================================
    // ADVANCED DSA: Custom Thread-Safe Trie (Prefix Tree)
    // O(M) Time Complexity for Search Autocomplete
    // ==========================================
    private static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        Set<Product> products = new HashSet<>();
    }

    private static class ProductTrie {
        private final TrieNode root = new TrieNode();
        private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

        public void insert(Product product) {
            if (product.getName() == null) return;
            rwLock.writeLock().lock();
            try {
                // Insert every word suffix for flexible sub-string matching
                String[] words = product.getName().toLowerCase().split("\\s+");
                for (String word : words) {
                    TrieNode current = root;
                    for (char ch : word.toCharArray()) {
                        current = current.children.computeIfAbsent(ch, k -> new TrieNode());
                        current.products.add(product);
                    }
                }
            } finally {
                rwLock.writeLock().unlock();
            }
        }

        public List<Product> search(String prefix) {
            if (prefix == null || prefix.trim().isEmpty()) return new ArrayList<>();
            rwLock.readLock().lock();
            try {
                String searchWord = prefix.toLowerCase().split("\\s+")[0]; // simplified to first word search
                TrieNode current = root;
                for (char ch : searchWord.toCharArray()) {
                    current = current.children.get(ch);
                    if (current == null) return new ArrayList<>(); // No match
                }
                return new ArrayList<>(current.products);
            } finally {
                rwLock.readLock().unlock();
            }
        }

        public void clear() {
            rwLock.writeLock().lock();
            try {
                root.children.clear();
                root.products.clear();
            } finally {
                rwLock.writeLock().unlock();
            }
        }
    }

    private final ProductTrie searchTrie = new ProductTrie();

    @PostConstruct
    public void initTrie() {
        refreshTrie();
    }

    private void refreshTrie() {
        searchTrie.clear();
        List<Product> all = repo.findByAvailableTrueOrderBySortOrderAsc();
        for (Product p : all) {
            searchTrie.insert(p);
        }
    }

    public List<Product> getAllAvailable() {
        return repo.findByAvailableTrueOrderBySortOrderAsc();
    }

    public List<Product> getAll() {
        return repo.findAllByOrderBySortOrderAsc();
    }

    public List<Product> getByCategory(String category) {
        return repo.findByCategoryIgnoreCaseAndAvailableTrue(category);
    }

    public Optional<Product> getById(Long id) {
        return repo.findById(id);
    }

    public List<Product> search(String name) {
        // O(M) lookup using Advanced DSA Trie instead of O(N) DB scan
        List<Product> trieResults = searchTrie.search(name);
        
        // Fallback to database if trie is empty/misses
        if (trieResults.isEmpty()) {
            return repo.findByNameContainingIgnoreCaseAndAvailableTrue(name);
        }
        return trieResults;
    }

    public Product save(Product product) {
        Product saved = repo.save(product);
        refreshTrie(); // Rebuild trie on change
        return saved;
    }

    public void delete(Long id) {
        repo.deleteById(id);
        refreshTrie(); // Rebuild trie on delete
    }

    public long getCount() { return repo.count(); }
}
