function CreateAccount(event) {
    if (event) {
        event.preventDefault();
    }
    
    let password = document.getElementById("password").value;
    let confirmpassword = document.getElementById("confirmpassword").value;
    let isvalid = true;
    
    if(password != confirmpassword) {
        let messageMatch = document.getElementById("pwid");
        messageMatch.style.color = "red";
        messageMatch.innerText = "Password Not Matched";
        isvalid = false;
    }

    if(password.length < 8) {
        let messageData = document.getElementById("pid");
        messageData.style.color = "red";
        messageData.innerText = "Password Length Should be Greater Than 8";
        isvalid = false;
    }

    let email = document.getElementById("emailid").value;
    if(!email.endsWith("@gmail.com")) {
        let message = document.getElementById("msg");
        message.style.color = "red";
        message.innerText = "Email Should End with @gmail.com";
        isvalid = false;
    }
    
    if(isvalid) {
        alert("Details Look Good, You Can Proceed With Account Creation !!");

        let uName = document.getElementById("username").value;
        let passwd = document.getElementById("password").value;
        let Eemail = document.getElementById("emailid").value;

        fetch('/createaccount', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username: uName, password: passwd, email: Eemail }),
        })
        .then(response => response.json())
.then(data => {
    // Hide the entire form
    document.getElementById('createac').style.display = 'none';

    // Create a container to align QR code and info side-by-side
    const qrContainer = document.createElement("div");
    qrContainer.style.display = "flex";
    qrContainer.style.justifyContent = "center";
    qrContainer.style.alignItems = "center";
    qrContainer.style.gap = "30px";
    qrContainer.style.marginTop = "20px";
    qrContainer.style.flexWrap = "wrap"; // For responsiveness

    // Create QR code image
    const qrImg = document.createElement("img");
    qrImg.id = "qrcode";
    qrImg.src = "data:image/png;base64," + data.qrCodeUrl;
    qrImg.style.width = "200px";
    qrImg.style.height = "200px";

    // Create info block
    const infoBox = document.createElement("div");
    infoBox.innerHTML = `
        <p style="font-size:16px; line-height: 1.6;">
            <strong>Instructions for Google Authenticator:</strong><br>
            📲 Open the Google Authenticator app.<br>
            📷 Scan the QR code shown on the left.<br>
            ✅ Save the generated 6-digit OTP.<br>
            🔐 This OTP will be required during login for extra security.
        </p>
    `;

    // Append QR and info side-by-side
    qrContainer.appendChild(qrImg);
    qrContainer.appendChild(infoBox);
    document.querySelector("h2").insertAdjacentElement('afterend', qrContainer);

    // Update the title
    document.querySelector("h2").innerText = "Registration Successful!";

    // Add Go to Login button
    const loginBtn = document.createElement("button");
    loginBtn.innerText = "Go to Login";
    loginBtn.style.marginTop = "30px";
    loginBtn.style.padding = "10px 30px";
    loginBtn.style.fontSize = "16px";
    loginBtn.style.backgroundColor = "orange";
    loginBtn.style.color = "white";
    loginBtn.style.border = "none";
    loginBtn.style.borderRadius = "5px";
    loginBtn.style.cursor = "pointer";
    loginBtn.onclick = () => {
        window.location.href = "/logauth";
    };

    // Center the button
    const btnWrapper = document.createElement("div");
    btnWrapper.style.display = "flex";
    btnWrapper.style.justifyContent = "center";
    btnWrapper.appendChild(loginBtn);
    qrContainer.insertAdjacentElement('afterend', btnWrapper);

    // Hide any submit buttons not inside the form
    const standaloneButtons = document.querySelectorAll("button[type='submit']");
    standaloneButtons.forEach(button => {
        if (!button.closest("#createac")) {
            button.style.display = "none";
        }
    });
})
        .catch(error => console.error('Error:', error));
    }
}