// Add this to your login handler function (in LogAuth.js)
function handlelogin(event) {
    event.preventDefault();

    let uName = document.getElementById("username").value;
    let passwd = document.getElementById("password").value;

    fetch('/loginsubmit', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username: uName, password: passwd }),
    })
    .then(response => response.json())
    .then(data => {
        console.log("Server response:", data);

        if (data.message === "Successfully Logged In") {
            // Save username to sessionStorage
            sessionStorage.setItem('currentUser', uName);
            alert("You are successfully logged in");
            window.location.href = "/otp1";
        } else {
            document.getElementById('responseMessage').style.color = 'red';
            document.getElementById('responseMessage').innerText = "Invalid username or password";
        }
    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById('responseMessage').style.color = 'red';
        document.getElementById('responseMessage').innerText = "Something went wrong. Please try again.";
    });
}