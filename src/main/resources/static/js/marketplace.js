document.addEventListener("DOMContentLoaded", () => {
    const logoutModal = document.querySelector("[data-modal-backdrop]");
    const logoutConfirm = document.querySelector("[data-modal-confirm]");
    let pendingLogoutUrl = null;

    const closeLogoutModal = () => {
        if (!logoutModal) {
            return;
        }
        logoutModal.classList.add("hidden");
        pendingLogoutUrl = null;
    };

    document.querySelectorAll("[data-confirm-logout]").forEach((link) => {
        link.addEventListener("click", (event) => {
            if (!logoutModal || !logoutConfirm) {
                return;
            }
            event.preventDefault();
            pendingLogoutUrl = link.href;
            logoutConfirm.href = pendingLogoutUrl;
            logoutModal.classList.remove("hidden");
        });
    });

    document.querySelectorAll("[data-modal-cancel]").forEach((button) => {
        button.addEventListener("click", closeLogoutModal);
    });

    if (logoutModal) {
        logoutModal.addEventListener("click", (event) => {
            if (event.target === logoutModal) {
                closeLogoutModal();
            }
        });
    }

    document.addEventListener("keydown", (event) => {
        if (event.key === "Escape") {
            closeLogoutModal();
        }
    });

    const paymentSelect = document.querySelector("[data-payment-select]");
    if (paymentSelect) {
        const updatePayment = () => {
            document.querySelectorAll("[data-payment]").forEach((panel) => {
                panel.classList.toggle("hidden", panel.dataset.payment !== paymentSelect.value);
            });
        };
        paymentSelect.addEventListener("change", updatePayment);
        updatePayment();
    }
});

window.addEventListener("pageshow", (event) => {
    if (event.persisted && (location.pathname === "/login" || location.pathname === "/register")) {
        location.reload();
    }
});
