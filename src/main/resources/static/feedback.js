document.addEventListener('DOMContentLoaded', function() {
    const feedbackForm = document.getElementById('feedbackForm');
    const successMessage = document.getElementById('successMessage');
    
    // Helper function to show error messages
    function showError(fieldId, message) {
        const errorElement = document.getElementById(fieldId + 'Error');
        if (errorElement) {
            errorElement.textContent = message;
            errorElement.style.display = 'block';
        }
    }
    
    // Helper function to hide error messages
    function hideError(fieldId) {
        const errorElement = document.getElementById(fieldId + 'Error');
        if (errorElement) {
            errorElement.style.display = 'none';
        }
    }
    
    // Helper function to validate email format
    function isValidEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }
    
    // Form validation
    function validateForm() {
        let isValid = true;
        
        // Validate Name
        const guestName = document.getElementById('guestName').value.trim();
        if (!guestName) {
            showError('guestName', 'Please enter your name');
            isValid = false;
        } else {
            hideError('guestName');
        }
        
        // Validate Email
        const email = document.getElementById('email').value.trim();
        if (!email) {
            showError('email', 'Please enter your email address');
            isValid = false;
        } else if (!isValidEmail(email)) {
            showError('email', 'Please enter a valid email address');
            isValid = false;
        } else {
            hideError('email');
        }
        
        // Validate Stay Date
        const stayDate = document.getElementById('stayDate').value;
        if (!stayDate) {
            showError('stayDate', 'Please select your stay date');
            isValid = false;
        } else {
            hideError('stayDate');
        }
        
        // Validate Overall Rating
        const overallRating = document.querySelector('input[name="overallRating"]:checked');
        if (!overallRating) {
            showError('overallRating', 'Please rate your overall experience');
            isValid = false;
        } else {
            hideError('overallRating');
        }
        
        return isValid;
    }
    
    // Handle form submission
    feedbackForm.addEventListener('submit', function(event) {
        event.preventDefault();
        
        // Validate form
        if (!validateForm()) {
            return;
        }
        
        // Collect form data
        const formData = {
            guestName: document.getElementById('guestName').value.trim(),
            email: document.getElementById('email').value.trim(),
            roomNumber: document.getElementById('roomNumber').value.trim(),
            stayDate: document.getElementById('stayDate').value,
            overallRating: document.querySelector('input[name="overallRating"]:checked').value,
            cleanlinessRating: document.querySelector('input[name="cleanlinessRating"]:checked')?.value || '3',
            serviceRating: document.querySelector('input[name="serviceRating"]:checked')?.value || '3',
            amenitiesRating: document.querySelector('input[name="amenitiesRating"]:checked')?.value || '3',
            comments: document.getElementById('comments').value.trim()
        };
        
        // Send data to the server
        fetch('/submitFeedback', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        })
        .then(response => response.json())
        .then(data => {
            // Show success message
            successMessage.style.display = 'block';
            // Reset form
            feedbackForm.reset();
            // Scroll to top to show success message
            window.scrollTo({ top: 0, behavior: 'smooth' });
            
            // Hide success message after 5 seconds
            setTimeout(() => {
                successMessage.style.display = 'none';
            }, 5000);
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Something went wrong. Please try again later.');
        });
    });
    
    // Clear error message when user starts typing
    const formInputs = feedbackForm.querySelectorAll('input, textarea');
    formInputs.forEach(input => {
        input.addEventListener('input', function() {
            hideError(this.id);
        });
    });
    
    // Clear error message when user selects a rating
    const ratingInputs = feedbackForm.querySelectorAll('input[type="radio"]');
    ratingInputs.forEach(input => {
        input.addEventListener('change', function() {
            const ratingName = this.name;
            hideError(ratingName);
        });
    });
});