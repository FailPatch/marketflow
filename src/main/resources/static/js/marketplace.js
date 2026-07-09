document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll("[data-confirm-logout]").forEach((link) => {
        link.addEventListener("click", (event) => {
            const confirmed = window.confirm("¿Seguro que deseas cerrar sesión?");
            if (!confirmed) {
                event.preventDefault();
            }
        });
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
