function validateOTP(event) {
  event.preventDefault();

  let uName = document.getElementById("username").value;
  let otp = document.getElementById("otp").value;

  fetch("/validateotp", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ username: uName, otp: otp }),
  })
    .then(response => response.json())
    .then(data => {
      if (data.ValidOtp === "Successfully verified") {
        alert("OTP verified successfully");
        window.location.href = "/";
      } else {
        alert("Invalid OTP. Please try again.");
      }
    })
    .catch(error => console.error('Error:', error));
}
