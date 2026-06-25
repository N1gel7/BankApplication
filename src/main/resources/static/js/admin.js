document.addEventListener("DOMContentLoaded", async () => {
    const userStr = localStorage.getItem("user");
    if (!userStr) {
        window.location.href = "login.html";
        return;
    }

    let user = JSON.parse(userStr);
    
    const welcomeMsg = document.getElementById("welcome-message");
    const alertBox = document.getElementById("alert-box");
    const kycTbody = document.querySelector("#kyc-table tbody");

    welcomeMsg.textContent = `Welcome back Admin, ${user.email}!`;

    function showAlert(message, type = "success") {
        alertBox.textContent = message;
        alertBox.className = `alert ${type}`;
        alertBox.classList.remove("hidden");
    }

    async function loadPendingKyc() {
        try {
            const res = await fetch("/api/v1/kyc/pending");
            if (res.ok) {
                const docs = await res.json();
                kycTbody.innerHTML = "";
                if (docs.length === 0) {
                    kycTbody.innerHTML = `<tr><td colspan="5" class="text-muted-center">No pending KYC requests.</td></tr>`;
                    return;
                }
                
                docs.forEach(doc => {
                    const tr = document.createElement("tr");
                    
                    tr.innerHTML = `
                        <td>${doc.id}</td>
                        <td>${doc.user.firstName} ${doc.user.lastName}</td>
                        <td>${doc.user.email}</td>
                        <td>-</td>
                        <td>
                            <button class="btn btn-primary btn-approve approve-btn" data-id="${doc.id}">Approve</button>
                        </td>
                    `;
                    kycTbody.appendChild(tr);
                });

                document.querySelectorAll(".approve-btn").forEach(btn => {
                    btn.addEventListener("click", async (e) => {
                        const id = e.target.getAttribute("data-id");
                        approveKyc(id);
                    });
                });

            } else {
                kycTbody.innerHTML = `<tr><td colspan="5" class="text-danger-center">Failed to load. Are you an Admin?</td></tr>`;
            }
        } catch (e) {
            kycTbody.innerHTML = `<tr><td colspan="5" class="text-danger-center">Network error.</td></tr>`;
        }
    }

    async function approveKyc(docId) {
        try {
            const res = await fetch(`/api/v1/kyc/${docId}/approve`, { method: "PATCH" });
            if (res.ok) {
                showAlert(`KYC Document #${docId} Approved!`, "success");
                loadPendingKyc();
            } else {
                showAlert(`Failed to approve KYC.`, "error");
            }
        } catch (e) {
            showAlert("Network error.", "error");
        }
    }

    async function loadTransactions() {
        const txnTbody = document.querySelector("#txn-table tbody");
        try {
            const res = await fetch("/api/v1/transactions");
            if (res.ok) {
                const txns = await res.json();
                txnTbody.innerHTML = "";
                if (txns.length === 0) {
                    txnTbody.innerHTML = `<tr><td colspan="5" class="text-muted-center">No transactions found.</td></tr>`;
                    return;
                }
                
                txns.reverse().forEach(txn => {
                    const tr = document.createElement("tr");
                    const date = new Date(txn.timestamp).toLocaleString();
                    
                    tr.innerHTML = `
                        <td>${txn.id || '-'}</td>
                        <td><strong>${txn.transactionType}</strong></td>
                        <td class="text-success">$${txn.amount.toFixed(2)}</td>
                        <td class="text-danger">$${txn.fee.toFixed(2)}</td>
                        <td>${date}</td>
                    `;
                    txnTbody.appendChild(tr);
                });
            } else {
                txnTbody.innerHTML = `<tr><td colspan="5" class="text-danger-center">Failed to load transactions.</td></tr>`;
            }
        } catch (e) {
            txnTbody.innerHTML = `<tr><td colspan="5" class="text-danger-center">Network error.</td></tr>`;
        }
    }

    document.getElementById("logout-btn").addEventListener("click", (e) => {
        e.preventDefault();
        localStorage.removeItem("user");
        window.location.href = "login.html";
    });

    loadPendingKyc();
    loadTransactions();
});
