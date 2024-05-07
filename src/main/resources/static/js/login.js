// Обработка формы входа
const loginForm = document.querySelector('.main-authentication form');
loginForm.addEventListener('submit', async (event) => {
    event.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const token = btoa(`${username}:${password}`);

    try {
        // Сохранение токена в localStorage
        localStorage.setItem('basicAuthToken', token);

        // Получение роли пользователя с бэкенда
        const response = await fetch('http://localhost:8080/users/user-role', {
            headers: {
                'Authorization': `Basic ${token}`
            }
        });

        if (response.ok) {
            const {role} = await response.json();
            console.log(response)
            // Перенаправление на соответствующую страницу
            switch (role) {
                case 'TRAINER':
                    window.location.href = 'trainer-dashboard.html';
                    break;
                case 'USER':
                    window.location.href = 'user-dashboard.html';
                    break;
                case 'ADMIN':
                    window.location.href = 'manager-dashboard.html';
                    break;
                default:
                    // Обработка ошибки, если роль не определена
                    console.error('Неизвестная роль пользователя');
            }
        } else {
            // Обработка ошибки при получении роли пользователя
            console.error('Ошибка при получении роли пользователя');
        }
    } catch (error) {
        // Обработка ошибки
        console.error('Ошибка при аутентификации:', error);
    }
});

// Использование токена Basic Auth в каждом запросе
async function fetchProtectedData() {
    const basicAuthToken = localStorage.getItem('basicAuthToken');
    if (basicAuthToken) {
        const response = await fetch('http://localhost:8080/users/profile', {
            headers: {
                'Authorization': `Basic ${basicAuthToken}`
            }
        });

        if (response.ok) {
            const data = await response.json();
            // Обработка полученных данных
        } else {
            // Обработка ошибки авторизации
        }
    } else {
        // Пользователь не авторизован, перенаправление на страницу входа
    }
}