// BakeryHub — Main JS
// Cart quantity validation, flash message auto-dismiss, smooth interactions

document.addEventListener('DOMContentLoaded', function () {

  // ── Auto-dismiss flash alerts ──────────────────
  const alerts = document.querySelectorAll('.alert-bakeryhub');
  alerts.forEach(alert => {
    setTimeout(() => {
      alert.style.opacity = '0';
      alert.style.transform = 'translateY(-10px)';
      alert.style.transition = 'all 0.5s ease';
      setTimeout(() => alert.remove(), 500);
    }, 4000);
  });

  // ── Quantity input validation ──────────────────
  document.querySelectorAll('.quantity-input').forEach(input => {
    input.addEventListener('change', function () {
      const val = parseInt(this.value);
      if (isNaN(val) || val < 1) this.value = 1;
      if (val > 99) this.value = 99;
    });
  });

  // ── Navbar scroll shadow ───────────────────────
  const navbar = document.querySelector('.navbar-bakeryhub');
  if (navbar) {
    window.addEventListener('scroll', () => {
      if (window.scrollY > 20) {
        navbar.style.boxShadow = '0 4px 24px rgba(0,0,0,.35)';
      } else {
        navbar.style.boxShadow = 'none';
      }
    });
  }

  // ── Product card micro-animation on load ────────
  const cards = document.querySelectorAll('.product-card');
  if ('IntersectionObserver' in window) {
    const observer = new IntersectionObserver(entries => {
      entries.forEach((entry, i) => {
        if (entry.isIntersecting) {
          setTimeout(() => {
            entry.target.style.opacity = '1';
            entry.target.style.transform = 'translateY(0)';
          }, i * 60);
          observer.unobserve(entry.target);
        }
      });
    }, { threshold: 0.08 });
    cards.forEach(card => {
      card.style.opacity = '0';
      card.style.transform = 'translateY(24px)';
      card.style.transition = 'opacity 0.5s ease, transform 0.5s ease, box-shadow 0.3s ease';
      observer.observe(card);
    });
  }

  // ── Add to Cart button feedback ────────────────
  document.querySelectorAll('.btn-add-cart[data-cart-btn]').forEach(btn => {
    btn.addEventListener('click', function (e) {
      const original = this.innerHTML;
      this.innerHTML = '✓ Added!';
      this.style.background = 'linear-gradient(135deg, #2e7d32, #4caf50)';
      setTimeout(() => {
        this.innerHTML = original;
        this.style.background = '';
      }, 1500);
    });
  });
});
