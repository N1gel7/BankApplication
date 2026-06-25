document.addEventListener("DOMContentLoaded", async () => {
    const userStr = localStorage.getItem("user");
    if (!userStr) {
        window.location.href = "login.html";
        return;
    }

    let user = JSON.parse(userStr);
    let currentAccount = null;

    const welcomeMsg = document.getElementById("welcome-message");
    const kycBanner = document.getElementById("kyc-banner");
    const kycActionSection = document.getElementById("kyc-action-section");
    const createAccountSection = document.getElementById("create-account-section");
    const activeAccountSection = document.getElementById("active-account-section");
    
    const alertBox = document.getElementById("alert-box");

    // Modals
    const depositModal = document.getElementById("deposit-modal");
    const transferModal = document.getElementById("transfer-modal");

    welcomeMsg.textContent = `Welcome back, ${user.email}!`;

    function showAlert(message, type = "success") {
        alertBox.textContent = message;
        alertBox.className = `alert ${type}`;
        alertBox.classList.remove("hidden");
    }

    function renderState() {
        kycActionSection.classList.add("hidden");
        createAccountSection.classList.add("hidden");
        activeAccountSection.classList.add("hidden");

        if (user.kycStatus === "UNSUBMITTED" || user.kycStatus === "REJECTED") {
            kycBanner.textContent = `KYC Status: ${user.kycStatus}`;
            kycBanner.className = "alert error";
            kycActionSection.classList.remove("hidden");
        } else if (user.kycStatus === "PENDING") {
            if (user.kycSubmitted) {
                kycBanner.textContent = "KYC Status: PENDING - Please wait for admin approval.";
                kycBanner.className = "alert warning";
                
                // Hide the file input form and just show the disabled button
                kycActionSection.classList.remove("hidden");
                const kycForm = document.getElementById("kyc-form");
                if(kycForm) {
                    kycForm.innerHTML = `<button class="btn btn-primary" disabled>Under Review</button>`;
                }
            } else {
                // If they haven't submitted yet in this browser session
                kycBanner.textContent = `KYC Status: ACTION REQUIRED`;
                kycBanner.className = "alert error";
                kycActionSection.classList.remove("hidden");
            }
        } else if (user.kycStatus === "APPROVED") {
            kycBanner.textContent = "KYC Status: APPROVED";
            kycBanner.className = "alert success";
            checkAccount();
        }
    }

    async function checkAccount() {
        try {
            const res = await fetch(`/api/v1/accounts/me`);
            if (res.ok) {
                currentAccount = await res.json();
                activeAccountSection.classList.remove("hidden");
                document.getElementById("display-account-type").textContent = currentAccount.accountType;
                document.getElementById("display-account-number").textContent = currentAccount.accountNumber;
                document.getElementById("display-balance").textContent = currentAccount.balance.toFixed(2);
                loadTransactions();
            } else {
                createAccountSection.classList.remove("hidden");
            }
        } catch (e) {
            console.error(e);
            createAccountSection.classList.remove("hidden");
        }
    }

    async function loadTransactions() {
        try {
            const res = await fetch(`/api/v1/transactions/me`);
            if (res.ok) {
                const txns = await res.json();
                const list = document.getElementById("transaction-list");
                list.innerHTML = "";
                if (txns.length === 0) {
                    list.innerHTML = `<li class="text-muted-center">No recent transactions.</li>`;
                    return;
                }
                txns.reverse().forEach(t => {
                    const li = document.createElement("li");
                    let sign = t.transactionType === "DEPOSIT" ? "+" : "-";
                    let colorClass = t.transactionType === "DEPOSIT" ? "text-success" : "text-dark";
                    
                    let feeText = t.fee > 0 ? ` (Fee: $${t.fee.toFixed(2)})` : "";
                    
                    li.innerHTML = `<strong>${t.transactionType}</strong> <span class="transaction-amount ${colorClass}">${sign}$${t.amount.toFixed(2)}${feeText}</span>`;
                    list.appendChild(li);
                });
            }
        } catch (e) {
            console.error("Failed to load txns", e);
        }
    }

    // Buttons and Forms
    document.getElementById("kyc-form")?.addEventListener("submit", async (e) => {
        e.preventDefault();
        try {
            const res = await fetch(`/api/v1/kyc/submit`, { method: "POST" });
            if (res.ok) {
                showAlert("KYC Submitted successfully!", "success");
                user.kycStatus = "PENDING";
                user.kycSubmitted = true;
                localStorage.setItem("user", JSON.stringify(user));
                renderState();
            } else {
                showAlert("Failed to submit KYC.", "error");
            }
        } catch (e) {
            showAlert("Network error.", "error");
        }
    });

    document.getElementById("create-account-btn").addEventListener("click", async () => {
        try {
            const res = await fetch(`/api/v1/accounts`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ accountType: "SAVINGS" })
            });
            if (res.ok) {
                showAlert("Account Created successfully!", "success");
                createAccountSection.classList.add("hidden");
                checkAccount();
            } else {
                showAlert("Failed to create account.", "error");
            }
        } catch (e) {
            showAlert("Network error.", "error");
        }
    });

    // Modal Toggles
    document.getElementById("open-deposit-modal").addEventListener("click", () => depositModal.classList.add("show"));
    document.getElementById("close-deposit").addEventListener("click", () => depositModal.classList.remove("show"));
    
    document.getElementById("open-transfer-modal").addEventListener("click", () => transferModal.classList.add("show"));
    document.getElementById("close-transfer").addEventListener("click", () => transferModal.classList.remove("show"));

    // Forms
    document.getElementById("deposit-form").addEventListener("submit", async (e) => {
        e.preventDefault();
        const amount = document.getElementById("deposit-amount").value;
        try {
            const res = await fetch(`/api/v1/transactions/deposit`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ accountNumber: currentAccount.accountNumber, amount: parseFloat(amount) })
            });
            if (res.ok) {
                showAlert("Deposit successful!", "success");
                depositModal.classList.remove("show");
                document.getElementById("deposit-form").reset();
                checkAccount();
            } else {
                showAlert("Deposit failed.", "error");
            }
        } catch (e) {
            showAlert("Network error.", "error");
        }
    });

    document.getElementById("transfer-form").addEventListener("submit", async (e) => {
        e.preventDefault();
        const amount = document.getElementById("transfer-amount").value;
        const receiver = document.getElementById("transfer-receiver").value;
        try {
            const res = await fetch(`/api/v1/transactions/transfer`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ accountNumber: receiver, amount: parseFloat(amount) })
            });
            if (res.ok) {
                showAlert("Transfer successful!", "success");
                transferModal.classList.remove("show");
                document.getElementById("transfer-form").reset();
                checkAccount();
            } else {
                showAlert("Transfer failed. Please check funds and account number.", "error");
            }
        } catch (e) {
            showAlert("Network error.", "error");
        }
    });

    document.getElementById("logout-btn").addEventListener("click", (e) => {
        e.preventDefault();
        localStorage.removeItem("user");
        window.location.href = "login.html";
    });

    renderState();
});
