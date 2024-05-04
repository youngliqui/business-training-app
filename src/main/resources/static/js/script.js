// Предполагается, что у вас есть элемент с ID 'registration-modal' в вашем HTML.
function openRegistrationModal() {
    document.getElementById('registration-modal').style.display = 'block';
}

// Ваша функция отправки формы
function submitRegistrationForm() {
    // Здесь код для обработки данных формы, например отправка на сервер
    alert('Вы успешно записаны на тренинг!');
}

// Добавление слушателей событий при загрузке страницы
document.addEventListener('DOMContentLoaded', function () {
    // Навешиваем обработчик на кнопку 'Записаться'
    var buttons = document.querySelectorAll('.btn-primary');
    buttons.forEach(function (button) {
        button.addEventListener('click', openRegistrationModal);
    });

    // Обработчик отправки формы
    var registrationForm = document.getElementById('registration-form');
    if (registrationForm) {
        registrationForm.addEventListener('submit', function (event) {
            event.preventDefault(); // Предотвращаем стандартное поведение формы
            submitRegistrationForm();
        });
    }
});
document.getElementById('settings-form').addEventListener('submit', function (event) {
    event.preventDefault();
    // Здесь код для обработки и сохранения настроек пользователя
    alert('Настройки сохранены.');
});
document.getElementById('upload-materials-form').addEventListener('submit', function (event) {
    event.preventDefault();
    // Здесь код для отправки файлов на сервер
    alert('Материалы загружены.');
});
document.getElementById('application-form').addEventListener('submit', function (event) {
    event.preventDefault();
    // Здесь код для обработки формы, например, отправка данных на сервер
    alert('Ваша заявка на тренинг отправлена на рассмотрение.');
    // После отправки формы, можно очистить поля или перенаправить на другую страницу
});
document.addEventListener('DOMContentLoaded', function () {
    const requestsContainer = document.querySelector('.incoming-requests');

    requestsContainer.addEventListener('click', function (e) {
        if (e.target.classList.contains('btn-accept')) {
            const request = e.target.closest('.request');
            request.remove();
            alert('Заявка принята.');
        } else if (e.target.classList.contains('btn-reject')) {
            const request = e.target.closest('.request');
            request.remove();
            alert('Заявка отклонена.');
        }
    });
});
document.addEventListener('DOMContentLoaded', function () {
    const usersList = document.getElementById('users-list');

    function addUserToTable(index, name, email, role) {
        const userRow = `
      <tr>
        <td>${index}</td>
        <td>${name}</td>
        <td>${email}</td>
        <td>${role}</td>
        <td>
          <button class="btn btn-primary btn-edit">Редактировать</button>
          <button class="btn btn-danger btn-delete">Удалить</button>
        </td>
      </tr>
    `;
        usersList.innerHTML += userRow;
    }

    addUserToTable(1, 'Иван Иванов', 'ivan@example.com', 'Пользователь');
    addUserToTable(2, 'Мария Петрова', 'maria@example.com', 'Пользователь');

    usersList.addEventListener('click', function (e) {
        if (e.target.classList.contains('btn-edit')) {
            // Логика для редактирования пользователя
            console.log('Редактировать пользователя');
        } else if (e.target.classList.contains('btn-delete')) {
            // Логика для удаления пользователя
            e.target.closest('tr').remove();
        }
    });
});
document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector('form');
    form.onsubmit = function (event) {
        event.preventDefault();

        // Формируем данные формы
        let formData = new FormData(form);

        // AJAX запрос на сервер
        fetch('path/to/server-side-script', {
            method: 'POST',
            body: formData,
        })
            .then(response => response.json())
            .then(data => {
                console.log(data); // Действия после успешной загрузки
            })
            .catch(error => {
                console.error(error); // Действия в случае ошибки
            });
    };
});
document.addEventListener('DOMContentLoaded', function () {
    // Находим все элементы с классом 'status approved'
    const approvedLinks = document.querySelectorAll('.status.approved');

    // Вешаем обработчик клика на каждый элемент
    approvedLinks.forEach(link => {
        link.addEventListener('click', function () {
            // Перенаправляем на страницу 'training-info.html'
            window.location.href = 'training-info.html';
        });
    });
});

