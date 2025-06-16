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

 document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('input[name="tipoUsuario"]').forEach(radio => {
        radio.addEventListener('change', function() {
            if (this.value === 'ORGANIZADOR') {
                document.getElementById('campoCPF').classList.add('d-none');
                document.getElementById('campoDataNasc').classList.add('d-none');
                document.getElementById('campoCNPJ').classList.remove('d-none');
                document.getElementById('campoEmpresa').classList.remove('d-none');
            } else {
                document.getElementById('campoCPF').classList.remove('d-none');
                document.getElementById('campoDataNasc').classList.remove('d-none');
                document.getElementById('campoCNPJ').classList.add('d-none');
                document.getElementById('campoEmpresa').classList.add('d-none');
            }
        });
    });
});
}
