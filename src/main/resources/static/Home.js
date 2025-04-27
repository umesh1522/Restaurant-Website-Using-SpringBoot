
 
      
        
        
        
  // Main initialization function when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    // Initialize all components
    initNavigation();
    initMobileMenu();
    initProfilePopup();
    loadSectionsContent();
});

// Function to handle navigation links
function initNavigation() {
    const navLinks = document.querySelectorAll('.nav-links a');
    
    navLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            // Get the section ID from the href attribute
            const sectionId = this.getAttribute('href').substring(1);
            
            // Find the target section
            const targetSection = document.getElementById(sectionId);
            
            // Only scroll if section exists
            if (targetSection) {
                // Scroll to the section
                targetSection.scrollIntoView({
                    behavior: 'smooth'
                });
                
                // Update active state
                navLinks.forEach(link => link.classList.remove('active'));
                this.classList.add('active');
                
                // Prevent default link behavior
                e.preventDefault();
            }
        });
    });
}

// Function to handle mobile menu
function initMobileMenu() {
    const menuToggle = document.getElementById('menu-toggle');
    const mobileNav = document.getElementById('mobileNav');
    
    if (menuToggle && mobileNav) {
        menuToggle.addEventListener('click', function(event) {
            event.stopPropagation();
            mobileNav.classList.toggle('active');
        });
        
        // Close mobile menu when clicking anywhere else
        document.addEventListener('click', function(event) {
            if (mobileNav.classList.contains('active') && 
                !mobileNav.contains(event.target) && 
                !menuToggle.contains(event.target)) {
                mobileNav.classList.remove('active');
            }
        });
        
        // Handle mobile navigation links
        const mobileNavLinks = mobileNav.querySelectorAll('a');
        mobileNavLinks.forEach(link => {
            link.addEventListener('click', function() {
                mobileNav.classList.remove('active');
            });
        });
    }
}

// Function to handle profile popup
function initProfilePopup() {
    const profileIcon = document.getElementById('profile-icon');
    const profilePopup = document.getElementById('profilePopup');
    const usernameSpan = document.getElementById('popupUsername');
    const logoutBtn = document.getElementById('logoutBtn');
    
    if (profileIcon && profilePopup) {
        // Get username from sessionStorage
        const currentUser = sessionStorage.getItem('currentUser');
        
        // Set the username in the popup if available
        if (currentUser && usernameSpan) {
            usernameSpan.textContent = currentUser;
        }
        
        // Toggle popup visibility when clicking on the profile icon
        profileIcon.addEventListener('click', function(event) {
            event.stopPropagation(); // Stop event from bubbling up
            profilePopup.classList.toggle('active');
        });
        
        // Close popup when clicking anywhere else on the page
        document.addEventListener('click', function(event) {
            if (!profilePopup.contains(event.target) && event.target !== profileIcon) {
                profilePopup.classList.remove('active');
            }
        });
        
        // Handle logout button click
        if (logoutBtn) {
            logoutBtn.addEventListener('click', function() {
                // Remove the stored username
                sessionStorage.removeItem('currentUser');
                // Redirect to login page
                window.location.href = '/logauth';
            });
        }
    }
}

// Function to load content from separate files
function loadSectionsContent() {
    loadContent('about-content', '/about.html');
   
    
}

// Function to load content into a specific element
function loadContent(elementId, filePath) {
    const contentElement = document.getElementById(elementId);
    
    if (contentElement) {
        fetch(filePath)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text();
            })
            .then(html => {
                contentElement.innerHTML = html;
            })
            .catch(error => {
                console.error('There was a problem loading the content:', error);
                contentElement.innerHTML = `<p>Error loading content. Please try again later.</p>`;
            });
    }
}

// Function to handle contact form submission
function submitContactForm(event) {
    event.preventDefault();
    
    // Get form values
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const subject = document.getElementById('subject').value;
    const message = document.getElementById('message').value;
    
    // Display loading state
    const submitBtn = document.querySelector('.send-btn');
    const originalBtnText = submitBtn.textContent;
    submitBtn.disabled = true;
    submitBtn.textContent = 'Sending...';
    
    // Send data to server
    fetch('/submitContactForm', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            name: name,
            email: email,
            subject: subject,
            message: message
        }),
    })
    .then(response => response.json())
    .then(data => {
        if (data.message === "Message sent successfully") {
            alert(`Thank you, ${name}! Your message has been sent. We'll get back to you shortly.`);
            document.getElementById('contactForm').reset();
        } else {
            alert('There was an error sending your message. Please try again.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('There was an error sending your message. Please try again.');
    })
    .finally(() => {
        // Restore button state
        submitBtn.disabled = false;
        submitBtn.textContent = originalBtnText;
    });
}

// Expose the submitContactForm function globally
window.submitContactForm = submitContactForm;



