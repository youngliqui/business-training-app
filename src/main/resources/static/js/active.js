// Функция для получения данных и обновления страницы
async function fetchAndDisplayData() {
    const basicAuthToken = localStorage.getItem('basicAuthToken');
    if (basicAuthToken) {
        const response = await fetch('http://localhost:8080/users/trainings/active', {
            headers: {
                'Authorization': `Basic ${basicAuthToken}`
            }
        });

        if (response.ok) {
            const data = await response.json();
            updateTrainingList(data); // Функция для обновления списка тренингов на странице
        } else {
            // Обработка ошибки авторизации
        }
    } else {
        // Пользователь не авторизован, перенаправление на страницу входа
    }
}

// Функция для обновления списка тренингов на странице
function updateTrainingList(data) {
    const trainingList = document.querySelector('.training-list');
    trainingList.innerHTML = ''; // Очистка текущего списка тренингов

    data.forEach(training => {
        const trainingItem = document.createElement('div');
        trainingItem.classList.add('training-item');

        const title = document.createElement('h2');
        title.textContent = training.title;

        const deadline = document.createElement('p');
        deadline.textContent = `Дедлайн: ${training.deadline}`;

        const detailsButton = document.createElement('button');
        detailsButton.classList.add('btn', 'btn-info');
        detailsButton.textContent = 'Подробнее';
        detailsButton.addEventListener('click', function() {
            toggleDetails(this);
        });

        const details = document.createElement('div');
        details.classList.add('training-details');
        details.innerHTML = `
            <p>Описание тренинга: ${training.description}</p>
            <p>Материалы для самостоятельной работы (здесь файлы для тренера)</p>
            <button class="btn btn-primary">Выгрузить домашнюю работу</button>
        `;

        const rateButton = document.createElement('button');
        rateButton.classList.add('btn', 'btn-primary');
        rateButton.textContent = 'Оценить';
        rateButton.setAttribute('data-bs-toggle', 'modal');
        rateButton.setAttribute('data-bs-target', '#rating-modal');

        trainingItem.appendChild(title);
        trainingItem.appendChild(deadline);
        trainingItem.appendChild(detailsButton);
        trainingItem.appendChild(details);
        trainingItem.appendChild(rateButton);

        trainingList.appendChild(trainingItem);
    });
}

// Функция для переключения отображения деталей тренинга
function toggleDetails(button) {
    var details = button.nextElementSibling;
    details.classList.toggle('show-details');
}

// Вызов функции для получения данных и обновления страницы
fetchAndDisplayData();