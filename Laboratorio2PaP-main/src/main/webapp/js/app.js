// Validación de formularios
document.addEventListener('DOMContentLoaded', function() {
    // Confirmación de contraseña
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        const password = form.querySelector('input[name="password"]');
        const confirmPassword = form.querySelector('input[name="confirmPassword"]');
        
        if (password && confirmPassword) {
            confirmPassword.addEventListener('input', function() {
                if (password.value !== confirmPassword.value) {
                    confirmPassword.setCustomValidity('Las contraseñas no coinciden');
                } else {
                    confirmPassword.setCustomValidity('');
                }
            });
        }
    });

    // Vista previa de archivo
    const fileInputs = document.querySelectorAll('input[type="file"]');
    fileInputs.forEach(input => {
        input.addEventListener('change', function() {
            const label = this.nextElementSibling;
            if (this.files && this.files[0]) {
                label.textContent = `📷 ${this.files[0].name}`;
                label.style.color = '#4facfe';
            }
        });
    });

    // Calculadora de costo en inscripción
    const touristCountInput = document.getElementById('touristCount');
    const totalCostSpan = document.getElementById('totalCost');
    
    if (touristCountInput && totalCostSpan) {
        const costPerPerson = Number(totalCostSpan.dataset.cost) || 0;
        
        touristCountInput.addEventListener('input', function() {
            const count = parseInt(this.value) || 0;
            totalCostSpan.textContent = (count * costPerPerson).toLocaleString();
        });
        touristCountInput.dispatchEvent(new Event('input'));
    }

    // Selección de tipo de registro
    const typeOptions = document.querySelectorAll('.type-option');
    typeOptions.forEach(option => {
        option.addEventListener('click', function() {
            typeOptions.forEach(opt => opt.classList.remove('active'));
            this.classList.add('active');
        });
    });

    // Mostrar mensajes de error/éxito
    const errorMessage = document.getElementById('errorMessage');
    const successMessage = document.getElementById('successMessage');
    
    if (errorMessage && errorMessage.textContent.trim()) errorMessage.style.display = 'block';
    if (successMessage && successMessage.textContent.trim()) successMessage.style.display = 'block';

    setTimeout(() => {
        if (errorMessage) errorMessage.style.display = 'none';
        if (successMessage) successMessage.style.display = 'none';
    }, 5000);
});

// Helpers de navegación (solo frontend)
function showPage(pageId) {
    const pages = document.querySelectorAll('.container, .main-content');
    pages.forEach(page => page.style.display = 'none');
    const targetPage = document.getElementById(pageId);
    if (targetPage) targetPage.style.display = 'block';
}

// Login form handling
document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.querySelector('form[action="LoginServlet"]');
    if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            // Basic validation
            const username = this.querySelector('#username').value.trim();
            const password = this.querySelector('#password').value.trim();
            
            if (!username || !password) {
                showMessage('error', 'Por favor complete todos los campos');
                return;
            }

            // AJAX call to login servlet
            fetch('LoginServlet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: new URLSearchParams({
                    username: username,
                    password: password
                })
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    showMessage('success', '¡Bienvenido!');
                    setTimeout(() => {
                        window.location.href = data.redirect || 'dashboard';
                    }, 1000);
                } else {
                    showMessage('error', data.message || 'Credenciales inválidas');
                }
            })
            .catch(error => {
                showMessage('error', 'Error al intentar iniciar sesión');
                console.error('Error:', error);
            });
        });
    }
});

// Utility function to show messages
function showMessage(type, message) {
    const messageDiv = document.createElement('div');
    messageDiv.className = `${type}-message`;
    messageDiv.style.display = 'block';
    messageDiv.textContent = message;
    
    // Remove any existing messages
    document.querySelectorAll('.error-message, .success-message')
        .forEach(el => el.remove());
    
    // Insert new message at the top of the form
    const loginSection = document.querySelector('.login-section');
    if (loginSection) {
        loginSection.insertBefore(messageDiv, loginSection.firstChild);
    }
    
    // Auto-hide after 5 seconds
    setTimeout(() => {
        messageDiv.style.display = 'none';
        setTimeout(() => messageDiv.remove(), 300);
    }, 5000);
}
