const trainingsListElement = document.querySelector(".trainer-trainings")
const editButton = document.querySelector(".button-edit-text")
const textArea = document.getElementById("bio")
let keyInput = true

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
            // console.log(data)
            return data
        }
    }
}

const generateTrainings = (training) => {
    const liEl = document.createElement("li")
    liEl.innerText = training
    return liEl
}

const showTrainings = (trainingsList) => {

    trainingsList.forEach(training => {
        trainingsListElement.append(generateTrainings(training))


    });
}

const showTrainingRequests = (waitedTrainings, approvedTrainings) => {
    waitedTrainings.forEach(waitedTraining => {
        const liEl = document.createElement("li")
        const spanEl = document.createElement("span")
        const spanElButton = document.createElement("span")

        spanEl.className = "training-name"
        liEl.className = "training-status"
        spanElButton.className = "status pending"

        spanEl.innerText = waitedTraining.topic
        spanElButton.innerText = "В ожидании"
        document.querySelector(".training-approves-and-waited").append(liEl)
        liEl.append(spanEl)
        liEl.append(spanElButton)
    })

    approvedTrainings.forEach(approvedTraining => {
        const liEl = document.createElement("li")
        const spanEl = document.createElement("span")
        const aEl = document.createElement("a")
        aEl.className = "status approved"
        aEl.href = "training-page.html"
        aEl.innerText = "Одобрено"
        spanEl.className = "training-name"
        liEl.className = "training-status"
        spanEl.innerText = approvedTraining.topic
        document.querySelector(".training-approves-and-waited").append(liEl)
        liEl.append(spanEl)
        liEl.append(aEl)
    })
}

const getApprovedTrainings = async () => {

    const basicAuthToken = localStorage.getItem('basicAuthToken')
    if (basicAuthToken) {
        const response = await fetch("http://localhost:8080/trainings/trainer", {
            headers: {
                'Authorization': `Basic ${basicAuthToken}`


            }
        })
        if (response.ok) {
            const data = await response.json()
            console.log(data)
            return data

        }
    }

}

const sendDescription = async (data) => {
    const basicAuthToken = localStorage.getItem('basicAuthToken')
    if (basicAuthToken) {
        const response = await fetch("http://localhost:8080/users/profile/description", {
            method: "POST",
            headers: {
                'Authorization': `Basic ${basicAuthToken}`,
                "Content-Type": "application/json"

            },
            body: data
        })
    }
}

const getWaitedTrainings = async () => {
    const basicAuthToken = localStorage.getItem('basicAuthToken')
    if (basicAuthToken) {
        const response = await fetch("http://localhost:8080/training-requests/trainer", {
            headers: {
                'Authorization': `Basic ${basicAuthToken}`


            }
        })
        if (response.ok) {
            const data = await response.json()
            console.log(data)
            return data

        }
    }

}


const setProfile = async () => {
    const profile = await getProfile()
    const response = await fetch(profile.imageLink)
    const blob = await response.blob()
    const imageUrl = URL.createObjectURL(blob)
    const image = document.querySelector(".profile-photo")
    const trainerName = profile.username
    const trainingsList = profile.trainingArchive
    const approveTrainings = await getApprovedTrainings()
    const waitedTrainings = await getWaitedTrainings()

    image.src = imageUrl


    for (let i = 0; i < 5; i++) {

        const ratEl = document.createElement("span")
        if (i < Math.round(profile.rating)) {
            ratEl.innerText = "★"

        } else {
            ratEl.innerText = "☆"
        }
        document.querySelector(".user-rating").append(ratEl)


    }


    document.getElementById("bio").innerText = profile.description
    document.querySelector(".trainer-name").innerText = trainerName
    showTrainings(trainingsList)
    showTrainingRequests(waitedTrainings, approveTrainings)
}

editButton.addEventListener("click", () => {
    if (keyInput == true) {
        textArea.disabled = false
        editButton.innerText = "Применить"
        keyInput = false

    } else {
        textArea.disabled = true
        editButton.innerText = "Редактировать"
        keyInput = true
        let data = JSON.stringify({
            description: textArea.value
        })
        sendDescription(data)
    }
})


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

const changeProfileImage = async (file) => {
    const basicAuthToken = localStorage.getItem('basicAuthToken');
    if (basicAuthToken) {
        const formData = new FormData();
        formData.append('file', file);

        try {
            const response = await fetch('http://localhost:8080/users/profile/image', {
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
        } catch (error) {
            console.error('Ошибка при изменении фотографии профиля:', error);
            alert('Произошла ошибка при изменении фотографии профиля. Пожалуйста, попробуйте еще раз.');
        }
    }
}


getWaitedTrainings()
getApprovedTrainings()
setProfile()
