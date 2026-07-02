document.addEventListener("DOMContentLoaded", () => {
    const registerForm = document.getElementById("register-form");
    const loginForm = document.getElementById("login-form");
    const alertBox = document.getElementById("alert-box");

    function showAlert(message, type = "success") {
        if (!alertBox) return;
        alertBox.textContent = message;
        alertBox.className = `alert ${type}`;
        alertBox.classList.remove("hidden");
    }

    function clearAlert() {
        if (!alertBox) return;
        alertBox.classList.add("hidden");
        alertBox.textContent = "";
        alertBox.className = "alert hidden";
    }

    if (registerForm) {
        registerForm.addEventListener("submit", async (e) => {
            e.preventDefault();
            clearAlert();

            const data = {
                firstName: registerForm.firstName.value,
                lastName: registerForm.lastName.value,
                email: registerForm.email.value,
                password: registerForm.password.value,
                phoneNumber: registerForm.phoneNumber.value,
                dob: registerForm.dob.value
            };

            try {
                const response = await fetch("/api/v1/auth/register", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(data)
                });

                if (response.ok) {
                    showAlert("Registration successful! Redirecting to login...", "success");
                    registerForm.reset();
                    setTimeout(() => {
                        window.location.href = "login.html";
                    }, 2000);
                } else {
                    showAlert("Registration failed. Please try again.", "error");
                }
            } catch (error) {
                console.error("Error:", error);
                showAlert("A network error occurred.", "error");
            }
        });
    }


    if (loginForm) {
        loginForm.addEventListener("submit", async (e) => {
            e.preventDefault();
            clearAlert();

            const data = {
                email: loginForm.email.value,
                password: loginForm.password.value
            };

            try {
                const response = await fetch("/api/v1/auth/login", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(data)
                });

                if (response.ok) {
                    const result = await response.json();
                    showAlert("Login successful! Redirecting...", "success");
                    localStorage.setItem("user", JSON.stringify(result.userDTO));
                    
                    const userRole = result.userDTO.role;
                    
                    setTimeout(() => {
                        if (userRole === "ADMIN" || userRole === "ROLE_ADMIN") {
                            window.location.href = "admin.html";
                        } else {
                            window.location.href = "dashboard.html";
                        }
                    }, 1000);
                } else if (response.status === 429) {
                    const errorData = await response.json();
                    showAlert(errorData.message || "Too many login attempts. Please try again later.", "error");
                } else {
                    const errorData = await response.json().catch(() => ({}));
                    showAlert(errorData.message || "Invalid email or password.", "error");
                }
            } catch (error) {
                console.error("Error:", error);
                showAlert("A network error occurred.", "error");
            }
        });
    }
});
