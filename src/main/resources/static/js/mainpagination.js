async function getData(currentPage, dateAscDateDesc, branches, trainer) {
    const url = `http://localhost:8080/trainings?page=${currentPage}&size=5&sortBy=${dateAscDateDesc}&branch=${branches}&trainer=${trainer}`
    console.log(url)
    const response = await fetch(`http://localhost:8080/trainings?page=${currentPage}&size=5&sortBy=${dateAscDateDesc}&branch=${branches}&trainer=${trainer}`)
    const data = await response.json()
    return data
}

let blockOfTrainings = document.getElementById("training-list")
const nextButton = document.getElementById("next-button")
const prevButton = document.getElementById("prev-button")
const selectDate = document.getElementById("sort-order")
const selectBranch = document.getElementById("industry-filter")
const trainerInput = document.getElementById("trainer-search")
const buttonTrainer = document.getElementById("button-trainer")
const sendName = document.getElementById("participant-name")
const sendEmail = document.getElementById("participant-email")
const sendNameAndEmail = document.getElementById("send-email-name")


let currentPage = 0
let dateAscDateDesk = "dateAsc"
let branches = ""
let trainerSearch = ""

const getBranch = () => {
    const selectedOptions = selectBranch.options[selectBranch.selectedIndex]
    if (selectedOptions.innerText == "Все отрасли") {
        return ""
    } else {
        console.log("Отрасль: " + selectedOptions.innerText)
        return selectedOptions.innerText
    }

}

const getNearFarDates = () => {
    const selectedOptions = selectDate.options[selectDate.selectedIndex]
    console.log(selectedOptions.innerText)
    if (selectedOptions.innerText == "Ближайшие даты") {
        return "dateAsc"
    }
    if (selectedOptions.innerText == "Дальние даты") {
        return "dateDesc"
    }

}
selectDate.addEventListener("change", () => {
    blockOfTrainings.innerText = ""
    dateAscDateDesk = getNearFarDates()
    showPages(currentPage, dateAscDateDesk, branches, trainerSearch)

})

selectBranch.addEventListener("change", () => {
    blockOfTrainings.innerText = ""
    branches = getBranch()
    console.log(branches)
    showPages(currentPage, dateAscDateDesk, branches, trainerSearch)

})

const getTrainerName = () => {
    const name = trainerInput.value
    return name
}
buttonTrainer.addEventListener("click", () => {
    blockOfTrainings.innerText = ""
    trainerSearch = getTrainerName()
    alert(trainerSearch)
    showPages(currentPage, dateAscDateDesk, branches, trainerSearch)

})

nextButton.addEventListener("click", async () => {
    blockOfTrainings.innerText = ""

    const trainings = await getData(currentPage + 1, dateAscDateDesk, branches, trainerSearch)

    if (trainings.length === 0) {
        nextButton.style.display = "none"
    } else {
        currentPage++
        showPages(currentPage, dateAscDateDesk, branches, trainerSearch)
    }
})

prevButton.addEventListener("click", () => {
    blockOfTrainings.innerText = ""


    if (currentPage == 0) {
        currentPage = 0
        console.log(currentPage)
    } else {
        currentPage--
        console.log(currentPage)
    }


    showPages(currentPage, dateAscDateDesk, branches, trainerSearch)
    nextButton.style.display = "block"

})


const showPages = async (currentPage, dateAscDateDesk, branches, trainer) => {
    const trainings = await getData(currentPage, dateAscDateDesk, branches, trainer)
    trainings.forEach(train => {
        const card = createTraining(train)
        blockOfTrainings.append(card)
    })
}

const getArray = async (currentPage) => {
    const trainings = await fetch(`http://localhost:8080/trainings?page=${currentPage}&size=5`)
    const data = await trainings.json()
    return data
}

const createTraining = train => {
    const card = document.createElement("div")
    const topic = document.createElement("h2")
    const trainer = document.createElement("p")
    const date = document.createElement("p")
    const branch = document.createElement("p")
    const buttonOpenDescription = document.createElement("button")
    const buttonReg = document.createElement("button")
    const trainDetails = document.createElement("div")
    const description = document.createElement("p")
    const price = document.createElement("p")
    const seats = document.createElement("p")

    card.className = "training-item"
    buttonOpenDescription.className = "btn btn-info"
    buttonReg.className = "btn btn-primary"


    topic.innerText = train.topic
    trainer.innerText = `Имя тренера: ${train.trainerName}`
    date.innerText = `Время проведения: ${train.dateTime}`
    description.innerText = `Описание: ${train.description}`
    description.style.display = "display:none"
    price.innerText = `Цена: ${train.price}`
    seats.innerText = `Всего мест: ${train.totalSeats}`
    branch.innerText = `Отрасль: ${train.branch}`
    buttonReg.innerText = "Записаться"
    buttonOpenDescription.innerText = "Подробнее"

    card.append(topic)
    card.append(trainer)
    card.append(date)
    card.append(branch)
    card.append(buttonOpenDescription)
    card.append(buttonReg)
    card.append(trainDetails)
    card.append(description)
    card.append(price)
    card.append(seats)

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

    buttonReg.addEventListener("click", () => {
        openRegistrationModal(train.id)

    })


    return card


}

function openRegistrationModal(id) {
    const modal = document.getElementById('registration-modal');
    modal.style.display = 'block'; // Отображаем модальное окно
    sendNameAndEmail.addEventListener("click", () => {
        sendInfo(id)
        modal.style.display = 'none'
    })
}


const sendInfo = async (id) => {
    if (!sendName.value || !sendEmail.value) {
        alert("Пожалуйста, заполните все поля");
        return;
    }
    const basicAuthToken = localStorage.getItem('basicAuthToken')
    const data = JSON.stringify({
        name: sendName.value,
        email: sendEmail.value,
    });

    if (basicAuthToken) {
        const response = await fetch(`http://localhost:8080/trainings/${id}/register`, {
            method: "POST",
            headers: {
                'Authorization': `Basic ${basicAuthToken}`,
                "Content-Type": "application/json"
            },
            body: data
        });
    }
};


showPages(currentPage, dateAscDateDesk, branches, trainerSearch)
