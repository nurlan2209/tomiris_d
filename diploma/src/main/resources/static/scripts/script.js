// Enhanced JavaScript for Medical Website

// Wait for DOM to be fully loaded
document.addEventListener('DOMContentLoaded', function() {
    // Sidebar toggle functionality
    initializeSidebar();
    
    // Smooth scrolling for anchor links
    initializeSmoothScrolling();
    
    // Add animation to disease cards
    animateDiseaseCards();
    
    // Initialize medicine carousel
    enhanceCarousel();
    
    // Chat functionality
    initializeChat();
});

// Initialize sidebar functionality
function initializeSidebar() {
    const toggleImage = document.getElementById('toggleImage');
    const hiddenBlock = document.getElementById('hiddenBlock');
    let isVisible = false;

    if (toggleImage && hiddenBlock) {
        toggleImage.addEventListener('click', function() {
            if (!isVisible) {
                hiddenBlock.style.display = 'block';
                setTimeout(() => {
                    hiddenBlock.style.left = '0';
                    toggleImage.style.left = '250px';
                }, 10);
            } else {
                hiddenBlock.style.left = '-300px';
                toggleImage.style.left = '0';
                setTimeout(() => {
                    hiddenBlock.style.display = 'none';
                }, 300);
            }
            isVisible = !isVisible;
        });

        // Close sidebar when clicking elsewhere
        document.addEventListener('click', function(event) {
            if (isVisible && event.target !== toggleImage && !hiddenBlock.contains(event.target)) {
                hiddenBlock.style.left = '-300px';
                toggleImage.style.left = '0';
                setTimeout(() => {
                    hiddenBlock.style.display = 'none';
                }, 300);
                isVisible = false;
            }
        });
    }
}

// Smooth scrolling for anchor links
function initializeSmoothScrolling() {
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function(e) {
            e.preventDefault();
            
            const targetId = this.getAttribute('href');
            if (targetId === '#') return;
            
            const targetElement = document.querySelector(targetId);
            if (targetElement) {
                window.scrollTo({
                    top: targetElement.offsetTop - 80, // Adjust for header/menu height
                    behavior: 'smooth'
                });
            }
        });
    });
}

// Animate disease cards on scroll
function animateDiseaseCards() {
    const diseaseCards = document.querySelectorAll('.diease');
    
    if (diseaseCards.length > 0) {
        // Check if IntersectionObserver is supported
        if ('IntersectionObserver' in window) {
            const observer = new IntersectionObserver((entries) => {
                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        entry.target.style.opacity = '1';
                        entry.target.style.transform = 'translateY(0)';
                        observer.unobserve(entry.target);
                    }
                });
            }, { threshold: 0.1 });
            
            // Set initial styles and observe each card
            diseaseCards.forEach(card => {
                card.style.opacity = '0';
                card.style.transform = 'translateY(50px)';
                card.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
                observer.observe(card);
            });
        } else {
            // Fallback for browsers that don't support IntersectionObserver
            diseaseCards.forEach(card => {
                card.style.opacity = '1';
                card.style.transform = 'translateY(0)';
            });
        }
    }
}

// Enhance the medicine carousel
function enhanceCarousel() {
    const carousel = document.querySelector('.carousel');
    
    if (carousel) {
        // Clone carousel items for infinite loop
        const items = carousel.querySelectorAll('.carousel-item');
        items.forEach(item => {
            const clone = item.cloneNode(true);
            carousel.appendChild(clone);
        });
        
        // Add hover pause functionality
        carousel.addEventListener('mouseenter', () => {
            carousel.style.animationPlayState = 'paused';
        });
        
        carousel.addEventListener('mouseleave', () => {
            carousel.style.animationPlayState = 'running';
        });
    }
}

// Initialize chat functionality
function initializeChat() {
    const chatForm = document.querySelector('.chat-form');
    const chatInput = document.querySelector('.chat-form input[type="text"]');
    const chatWindow = document.querySelector('.chat-window');
    
    if (chatForm && chatInput && chatWindow) {
        // Scroll to bottom of chat window
        chatWindow.scrollTop = chatWindow.scrollHeight;
        
        // Auto-expand input field
        chatInput.addEventListener('input', function() {
            this.style.height = 'auto';
            this.style.height = (this.scrollHeight) + 'px';
        });
        
        // Add loading animation while waiting for response
        chatForm.addEventListener('submit', function() {
            // Create loading message
            const loadingDiv = document.createElement('div');
            loadingDiv.className = 'chat-message';
            
            const loadingMessage = document.createElement('div');
            loadingMessage.className = 'bot-message loading';
            loadingMessage.innerHTML = '<span class="dot"></span><span class="dot"></span><span class="dot"></span>';
            
            loadingDiv.appendChild(loadingMessage);
            
            // Add to chat window
            const chatMessages = document.querySelector('.chat-messages');
            if (chatMessages) {
                chatMessages.appendChild(loadingDiv);
                chatWindow.scrollTop = chatWindow.scrollHeight;
            }
        });
    }
}

// Add animation to page transitions
window.addEventListener('pageshow', function(event) {
    if (event.persisted) {
        document.body.style.display = 'none';
        setTimeout(function() {
            document.body.style.display = 'block';
        }, 10);
    }
});