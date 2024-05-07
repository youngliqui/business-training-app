const blockOfTrainings = document.getElementById("incoming-requests")


const createTraining = train => {
    const card = document.createElement("div")
    const topic = document.createElement("h2")
    const trainer = document.createElement("p")
    const date = document.createElement("p")
    const branch = document.createElement("p")
    const buttonOpenDescription = document.createElement("button")
    const buttonAccept = document.createElement("submit")
    const buttonReject = document.createElement("submit")
    const trainDetails = document.createElement("div")
    const description = document.createElement("p")
    const price = document.createElement("p")
    const seats = document.createElement("p")


    card.className = "training-item"
    buttonOpenDescription.className = "btn btn-info"
    buttonAccept.className = "btn btn-success btn-accept"
    buttonReject.className = "btn btn-danger btn-reject"


    topic.innerText = train.topic
    trainer.innerText = `Имя тренера: ${train.trainerName}`
    date.innerText = `Время проведения: ${train.dateTime}`
    description.innerText = `Описание: ${train.description}`
    description.style.display = "display:none"
    price.innerText = `Цена: ${train.price}`
    seats.innerText = `Всего мест: ${train.totalSeats}`
    branch.innerText = `Отрасль: ${train.branch}`
    buttonOpenDescription.innerText = "Описание"
    buttonAccept.innerText = "Принять"
    buttonReject.innerText = "Отклонить"

    card.append(topic)
    card.append(trainer)
    card.append(date)
    card.append(branch)
    card.append(buttonOpenDescription)
    card.append(buttonAccept)
    card.append(buttonReject)
    card.append(trainDetails)
    card.append(description)
    card.append(price)
    card.append(seats)
    card.className = "request"

    description.style.display = "none"
    let showKey = true

    price.style.display = "none"
    seats.style.display = "none"




    buttonOpenDescription.addEventListener("click", () => {
        if (showKey) {
            description.style.display = "block"
            price.style.display = "block"
            seats.style.display = "block"
            showKey = false
        } else {
            description.style.display = "none"
            price.style.display = "none"
            seats.style.display = "none"
            showKey = true
        }

    })

    buttonAccept.addEventListener("click", () => {
        approveTraining(train.id)
        window.location.reload()
    })

    buttonReject.addEventListener("click", () => {
        rejectTraining(train.id)
        window.location.reload()
    })




    return card


}


const getProfile = async () => {
    const basicAuthToken = localStorage.getItem('basicAuthToken')
    if (basicAuthToken) {
        const response = await fetch('http://localhost:8080/users/profile', {
            headers: {
                'Authorization': `Basic ${basicAuthToken}`
            }
        });

        if (response.ok) {
            const data = await response.json()
            console.log(data)
            return data

        }
    }
}


const getTrainings = async () => {
    const basicAuthToken = localStorage.getItem('basicAuthToken')
    if (basicAuthToken) {
        const response = await fetch('http://localhost:8080/training-requests', {
            headers: {
                'Authorization': `Basic ${basicAuthToken}`
            }
        });

        if (response.ok) {
            const data = await response.json()
            console.log(data)
            return data

        }
    }

}

const showPages = async () => {
    const trainings = await getTrainings()
    trainings.forEach(train => {
        const card = createTraining(train)
        blockOfTrainings.append(card)
    })
}

const setProfile = async () => {
    const profile = await getProfile()
    const response = await fetch(profile.imageLink)
    const blob = await response.blob()
    const imageUrl = URL.createObjectURL(blob)


    const image = document.querySelector(".profile-photo")
    image.src = imageUrl

    document.querySelector(".admin-name").innerText = profile.username
}

const changeProfileImage = async (file) => {
    const basicAuthToken = localStorage.getItem('basicAuthToken');
    if (basicAuthToken) {
        const formData = new FormData();
        formData.append('file', file);

        const response = await fetch('http://localhost:8080/profile/image', {
            method: 'POST',
            headers: {
                'Authorization': `Basic ${basicAuthToken}`
            },
            body: formData
        });

        if (response.ok) {
            const profile = await getProfile();
            const responseImage = await fetch(profile.imageLink);
            const blob = await responseImage.blob();
            const imageUrl = URL.createObjectURL(blob);

            const image = document.querySelector(".profile-photo");
            image.src = imageUrl;

            alert('Фотография профиля успешно изменена!');
        } else {
            alert('Ошибка при изменении фотографии профиля');
        }
    }
}

const changePhotoButton = document.querySelector(".btn-change-photo");
changePhotoButton.addEventListener("click", () => {
    const fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.accept = 'image/*';
    fileInput.onchange = (event) => {
        const file = event.target.files[0];
        changeProfileImage(file);
    };
    fileInput.click();
});

const approveTraining = async (id) => {
    const basicAuthToken = localStorage.getItem('basicAuthToken')
    if (basicAuthToken) {
        const response = await fetch(`http://localhost:8080/training-requests/${id}/approve`, {
            method: "POST",
            headers: {
                'Authorization': `Basic ${basicAuthToken}`
            }
        });

    }
}

const rejectTraining = async (id) => {
    const basicAuthToken = localStorage.getItem('basicAuthToken')
    if (basicAuthToken) {
        const response = await fetch(`http://localhost:8080/training-requests/${id}/reject`, {
            method: "POST",
            headers: {
                'Authorization': `Basic ${basicAuthToken}`
            }
        });
    }
}



setProfile()
getProfile()
getTrainings()
showPages()
