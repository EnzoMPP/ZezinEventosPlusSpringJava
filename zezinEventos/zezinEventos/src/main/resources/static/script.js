// ===== FUNÇÕES GERAIS =====

// Toggle de visualização de senha
function togglePassword(fieldId = 'password', iconId = 'toggleIcon') {
    const passwordField = document.getElementById(fieldId);
    const toggleIcon = document.getElementById(iconId);

    if (passwordField) {
        if (passwordField.type === 'password') {
            passwordField.type = 'text';
            if (toggleIcon) {
                toggleIcon.className = 'bi bi-eye-slash';
            }
        } else {
            passwordField.type = 'password';
            if (toggleIcon) {
                toggleIcon.className = 'bi bi-eye';
            }
        }
    }
}

// Atualizar hora atual
function updateTime() {
    const now = new Date();
    const timeElement = document.getElementById('currentTime');
    if (timeElement) {
        timeElement.textContent = now.toLocaleString('pt-BR');
    }
}

// Auto-hide alerts após 5 segundos
function autoHideAlerts() {
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        if (alert.classList.contains('alert-success') || alert.classList.contains('alert-warning')) {
            setTimeout(() => {
                alert.style.transition = 'opacity 0.5s ease';
                alert.style.opacity = '0';
                setTimeout(() => {
                    alert.remove();
                }, 500);
            }, 5000);
        }
    });
}

// ===== MÁSCARAS DE FORMATAÇÃO =====

// Máscara para telefone
function aplicarMascaraTelefone(elemento) {
    elemento.addEventListener('input', function (e) {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length <= 11) {
            value = value.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3');
            if (value.length < 14) {
                value = value.replace(/(\d{2})(\d{4})(\d{4})/, '($1) $2-$3');
            }
            e.target.value = value;
        }
    });
}

// Máscara para CPF
function aplicarMascaraCPF(elemento) {
    elemento.addEventListener('input', function (e) {
        let value = e.target.value.replace(/\D/g, '');
        value = value.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
        e.target.value = value;
    });
}

// Máscara para CNPJ
function aplicarMascaraCNPJ(elemento) {
    elemento.addEventListener('input', function (e) {
        let value = e.target.value.replace(/\D/g, '');
        value = value.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/, '$1.$2.$3/$4-$5');
        e.target.value = value;
    });
}

// ===== VALIDAÇÕES =====

// Validação de senhas coincidentes
function validarSenhasCoincidentes() {
    const senhaField = document.getElementById('senha');
    const confirmarSenhaField = document.getElementById('confirmarSenha') || document.getElementById('confirmSenha');

    if (!senhaField || !confirmarSenhaField) return;

    function validar() {
        const senha = senhaField.value;
        const confirmarSenha = confirmarSenhaField.value;

        if (senha && senha.length < 6) {
            senhaField.setCustomValidity('A senha deve ter pelo menos 6 caracteres');
        } else {
            senhaField.setCustomValidity('');
        }

        if (senha && confirmarSenha && senha !== confirmarSenha) {
            confirmarSenhaField.setCustomValidity('As senhas não conferem');
        } else {
            confirmarSenhaField.setCustomValidity('');
        }
    }

    senhaField.addEventListener('input', validar);
    confirmarSenhaField.addEventListener('input', validar);
}

// Validação do login (remover caracteres especiais)
function validarLogin() {
    const loginField = document.getElementById('login');
    if (loginField) {
        loginField.addEventListener('input', function (e) {
            e.target.value = e.target.value.replace(/[^a-zA-Z0-9._-]/g, '');
        });
    }
}

// ===== CONFIRMAÇÕES =====

// Confirmar exclusões
function confirmarExclusao(tipo = 'item') {
    return confirm(`Tem certeza que deseja excluir este ${tipo}?\nEsta ação não pode ser desfeita!`);
}

// Confirmar desativação de usuário
function confirmarDesativacao() {
    return confirm('Tem certeza que deseja desativar este usuário?\nEle não poderá mais acessar o sistema.');
}

// Confirmação de criação de admin
function confirmarCriacaoAdmin(nome, login) {
    return confirm(`Tem certeza que deseja criar um novo administrador?\n\nNome: ${nome}\nLogin: ${login}\n\nEsta ação dará acesso total ao sistema para este usuário.`);
}

// ===== ANIMAÇÕES =====

// Animação das barras de progresso
function animarBarrasProgresso() {
    const progressBars = document.querySelectorAll('.progress-bar');
    progressBars.forEach(bar => {
        const width = bar.style.width;
        bar.style.width = '0%';
        setTimeout(() => {
            bar.style.width = width;
        }, 100);
    });
}

// ===== CADASTRO - ALTERNAR CAMPOS =====

// Alternar campos de cadastro baseado no tipo de usuário
function alternarCamposCadastro() {
    document.querySelectorAll('input[name="tipoUsuario"]').forEach(radio => {
        radio.addEventListener('change', function () {
            const campoCPF = document.getElementById('campoCPF');
            const campoDataNasc = document.getElementById('campoDataNasc');
            const campoCNPJ = document.getElementById('campoCNPJ');
            const campoEmpresa = document.getElementById('campoEmpresa');

            if (this.value === 'ORGANIZADOR') {
                // Mostrar campos do organizador
                if (campoCPF) campoCPF.classList.add('d-none');
                if (campoDataNasc) campoDataNasc.classList.add('d-none');
                if (campoCNPJ) campoCNPJ.classList.remove('d-none');
                if (campoEmpresa) campoEmpresa.classList.remove('d-none');
            } else {
                // Mostrar campos do cliente
                if (campoCPF) campoCPF.classList.remove('d-none');
                if (campoDataNasc) campoDataNasc.classList.remove('d-none');
                if (campoCNPJ) campoCNPJ.classList.add('d-none');
                if (campoEmpresa) campoEmpresa.classList.add('d-none');
            }
        });
    });
}

// ===== VALIDAÇÕES DE EVENTOS =====

// Validação de data de evento
function validarDataEvento() {
    const dataEventoField = document.getElementById('dataEvento');
    if (dataEventoField) {
        dataEventoField.addEventListener('change', function () {
            const selectedDate = new Date(this.value);
            const now = new Date();

            if (selectedDate <= now) {
                alert('A data do evento deve ser no futuro!');
                this.focus();
            }
        });
    }
}

// Validação de vagas
function validarVagas() {
    const vagasTotaisField = document.getElementById('vagasTotais');
    if (vagasTotaisField) {
        vagasTotaisField.addEventListener('change', function () {
            const vagasOcupadasField = document.getElementById('vagasOcupadas');
            if (vagasOcupadasField) {
                const vagasOcupadas = parseInt(vagasOcupadasField.value) || 0;
                const vagasTotais = parseInt(this.value) || 0;

                if (vagasTotais < vagasOcupadas) {
                    alert('O total de vagas não pode ser menor que as vagas já ocupadas!');
                    this.value = vagasOcupadas;
                }
            }
        });
    }
}

// Validação de preço
function validarPreco() {
    const precoIngressoField = document.getElementById('precoIngresso');
    if (precoIngressoField) {
        precoIngressoField.addEventListener('input', function () {
            let value = parseFloat(this.value);
            if (isNaN(value) || value < 0) {
                this.value = '0.00';
            }
        });
    }
}

// ===== IMPRESSÃO =====

window.addEventListener('beforeprint', function () {
    document.body.classList.add('printing');
});

window.addEventListener('afterprint', function () {
    document.body.classList.remove('printing');
});

// ===== INICIALIZAÇÃO =====

// Executar quando o DOM estiver carregado
document.addEventListener('DOMContentLoaded', function () {
    // Atualizar hora (se elemento existir)
    updateTime();
    setInterval(updateTime, 1000);

    // Auto-hide de alertas
    autoHideAlerts();

    // Aplicar máscaras
    const telefoneFields = document.querySelectorAll('#telefone, input[name="telefone"]');
    telefoneFields.forEach(field => aplicarMascaraTelefone(field));

    const cpfFields = document.querySelectorAll('#cpf, input[name="cpf"]');
    cpfFields.forEach(field => aplicarMascaraCPF(field));

    const cnpjFields = document.querySelectorAll('#cnpj, input[name="cnpj"]');
    cnpjFields.forEach(field => aplicarMascaraCNPJ(field));

    // Validações
    validarSenhasCoincidentes();
    validarLogin();
    validarDataEvento();
    validarVagas();
    validarPreco();

    // Alternar campos de cadastro
    alternarCamposCadastro();

    // Animar barras de progresso
    animarBarrasProgresso();

    // Auto-focus em campos de busca
    const buscaField = document.getElementById('busca');
    if (buscaField) {
        buscaField.focus();
    }

    // Configurar toggle de senha (múltiplos campos)
    const togglePasswordBtn = document.getElementById('togglePassword');
    if (togglePasswordBtn) {
        togglePasswordBtn.addEventListener('click', () => togglePassword());
    }

    // Configurar confirmações de exclusão
    document.querySelectorAll('form[onsubmit*="confirm"]').forEach(form => {
        form.addEventListener('submit', function (e) {
            const isDelete = this.action.includes('/excluir/') || this.action.includes('/desativar/');

            if (isDelete) {
                const tipo = this.action.includes('/usuarios/') ? 'usuário' : 'item';
                if (!confirmarExclusao(tipo)) {
                    e.preventDefault();
                }
            }
        });
    });

    // Validação de formulário de edição de usuário
    const formEdicao = document.querySelector('form[action*="/editar/"]');
    if (formEdicao) {
        formEdicao.addEventListener('submit', function (e) {
            const senhaField = document.getElementById('senha');
            const confirmarSenhaField = document.getElementById('confirmarSenha') || document.getElementById('confirmSenha');

            if (senhaField && confirmarSenhaField) {
                const senha = senhaField.value;
                const confirmarSenha = confirmarSenhaField.value;

                if (senha && (!confirmarSenha || senha !== confirmarSenha)) {
                    e.preventDefault();
                    alert('Por favor, confirme a nova senha corretamente.');
                    confirmarSenhaField.focus();
                }
            }
        });
    }
});

// ===== FUNÇÕES ESPECÍFICAS PARA DIFERENTES PÁGINAS =====

// Para a página de login - toggle de senha específico
function togglePasswordLogin() {
    const senhaInput = document.getElementById('password');
    const toggleIcon = document.getElementById('toggleIcon');

    if (senhaInput && toggleIcon) {
        if (senhaInput.type === 'password') {
            senhaInput.type = 'text';
            toggleIcon.className = 'bi bi-eye-slash';
        } else {
            senhaInput.type = 'password';
            toggleIcon.className = 'bi bi-eye';
        }
    }
}

// Para múltiplos campos de senha (cadastro)
function togglePasswordField(inputId, iconId) {
    const input = document.getElementById(inputId);
    const icon = document.getElementById(iconId);

    if (input && icon) {
        if (input.type === 'password') {
            input.type = 'text';
            icon.classList.remove('bi-eye');
            icon.classList.add('bi-eye-slash');
        } else {
            input.type = 'password';
            icon.classList.remove('bi-eye-slash');
            icon.classList.add('bi-eye');
        }
    }
}

// Auto-refresh da página a cada 30 segundos (para ver atualizações em tempo real)
setTimeout(function () {
    location.reload();
}, 30000);

// Confirmar ação de promover
function confirmarPromocao() {
    return confirm('Tem certeza que deseja promover o próximo cliente da fila?\n\nEsta ação irá:\n1. Inscrever o cliente no evento\n2. Remover da lista de espera\n3. Reorganizar as posições');
}