function togglePassword() {
    const senhaInput = document.getElementById('senha');
    const toggleIcon = document.getElementById('toggleIcon');

    if (senhaInput.type === 'password') {
        senhaInput.type = 'text';
        toggleIcon.className = 'bi bi-eye-slash';
    } else {
        senhaInput.type = 'password';
        toggleIcon.className = 'bi bi-eye';
    }
}
