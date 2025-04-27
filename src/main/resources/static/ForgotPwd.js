document.addEventListener("DOMContentLoaded", function () {
  document.getElementById("formid").addEventListener("submit", handleForgotPassword);
});

async function handleForgotPassword(event) {
  event.preventDefault(); // Prevents default form submission

  // Get input values
  let email = document.getElementById("username").value.trim();
  let newPassword = document.getElementById("newpsd").value.trim();
  let confirmPassword = document.getElementById("confirmpwd").value.trim();
  let responseMessage = document.getElementById("responseMessage");

  // Validate fields
  if (!email || !newPassword || !confirmPassword) {
    responseMessage.textContent = "All fields are required!";
    responseMessage.style.color = "red";
    return;
  }
  
  if (newPassword !== confirmPassword) {
    responseMessage.textContent = "Passwords do not match!";
    responseMessage.style.color = "red";
    return;
  }

  // API request payload
  let payload = {
    email: email,
    password: newPassword
  };

  try {
    let response = await fetch("/forgotpassincontroller", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(payload)
    });

    let data = await response.json();

    if (response.ok) {
      responseMessage.textContent = data.message;
      responseMessage.style.color = "green";
    } else {
      responseMessage.textContent = data.message || "An error occurred.";
      responseMessage.style.color = "red";
    }
  } catch (error) {
    responseMessage.textContent = "Failed to connect to the server.";
    responseMessage.style.color = "red";
  }
  
}
