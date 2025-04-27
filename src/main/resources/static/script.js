document.addEventListener('DOMContentLoaded', function() {
    const registrationForm = document.getElementById('registrationForm');
    const otpForm = document.getElementById('otpForm');
    const successMessage = document.getElementById('successMessage');
    
    const registerBtn = document.getElementById('registerBtn');
    const verifyBtn = document.getElementById('verifyBtn');
    
    const registerError = document.getElementById('registerError');
    const otpError = document.getElementById('otpError');
    
    // Handle registration form submission
    registerBtn.addEventListener('click', function() {
        const username = document.getElementById('username').value.trim();
        const email = document.getElementById('email').value.trim();
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        
        // Reset error message
        registerError.textContent = '';
        
        // Basic validation
        if (!username || !email || !password || !confirmPassword) {
            registerError.textContent = 'All fields are required';
            return;
        }
        
        if (password !== confirmPassword) {
            registerError.textContent = 'Passwords do not match';
            return;
        }
        
        // Email validation
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            registerError.textContent = 'Please enter a valid email address';
            return;
        }
        
        // Submit registration request
        registerBtn.disabled = true;
        registerBtn.textContent = 'Processing...';
        
        fetch('/createaccount', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: username,
                email: email,
                password: password
            })
        })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                // Show OTP verification form
                registrationForm.classList.add('hidden');
                otpForm.classList.remove('hidden');
                // Store email for OTP verification
                sessionStorage.setItem('registrationEmail', email);
            } else {
                registerError.textContent = data.message || 'Registration failed. Please try again.';
                registerBtn.disabled = false;
                registerBtn.textContent = 'Register';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            registerError.textContent = 'Something went wrong. Please try again later.';
            registerBtn.disabled = false;
            registerBtn.textContent = 'Register';
        });
    });
    
    // Handle OTP verification
    verifyBtn.addEventListener('click', function() {
        const otp = document.getElementById('otp').value.trim();
        const email = sessionStorage.getItem('registrationEmail');
        
        // Reset error message
        otpError.textContent = '';
        
        // Basic validation
        if (!otp) {
            otpError.textContent = 'Please enter the OTP';
            return;
        }
        
        if (otp.length !== 6 || isNaN(otp)) {
            otpError.textContent = 'OTP must be 6 digits';
            return;
        }
        
        // Submit OTP verification request
        verifyBtn.disabled = true;
        verifyBtn.textContent = 'Verifying...';
        
        fetch('/verifyotp', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                email: email,
                otp: otp
            })
        })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                // Show success message
                otpForm.classList.add('hidden');
                successMessage.classList.remove('hidden');
                // Clean up session storage
                sessionStorage.removeItem('registrationEmail');
            } else {
                otpError.textContent = data.message || 'OTP verification failed. Please try again.';
                verifyBtn.disabled = false;
                verifyBtn.textContent = 'Verify OTP';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            otpError.textContent = 'Something went wrong. Please try again later.';
            verifyBtn.disabled = false;
            verifyBtn.textContent = 'Verify OTP';
        });
    });
});