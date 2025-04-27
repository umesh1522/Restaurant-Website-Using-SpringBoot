function handleForgotUsername(event) {
    event.preventDefault();

    const email = document.getElementById("email").value;

    fetch("/forgotusernamecontroller", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ email: email })
    })
    .then(response => response.json())
    .then(data => {
        document.getElementById("responseMessage").innerText = data.message;
    })
    .catch(error => {
        console.error("Error:", error);
    });
}
