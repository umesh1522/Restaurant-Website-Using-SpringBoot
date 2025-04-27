function submitContactForm(event) {
    event.preventDefault();
    
    // Get form values
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const subject = document.getElementById('subject').value;
    const message = document.getElementById('message').value;
    
    // Disable submit button while processing
    const submitBtn = document.querySelector('.submit-btn');
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
        // Re-enable submit button
        submitBtn.disabled = false;
        submitBtn.textContent = 'Send Message';
    });
}