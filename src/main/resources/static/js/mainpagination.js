async function getData(currentPage ,dateAscDateDesc) {
    const response = await fetch(`http://localhost:8080/trainings?page=${currentPage}&size=5&sortBy=${dateAscDateDesc}`)
    const data = await response.json()
    return data
}






const blockOfTrainings = document.getElementById("training-list")
const nextButton = document.getElementById("next-button")
const prevButton = document.getElementById("prev-button")
const selectDate = document.getElementById("sort-order")
const selectBranch = document.getElementById("industry-filter")

let currentPage = 0
let dateAscDateDesk = ""

const getBranch = () => {
    const selectedOptions = selectBranch.options[selectBranch.selectedIndex]
    if(selectedOptions.innerText == "D"){
        return "dateAsc"
    }
    if (selectedOptions.innerText == "Дальние даты"){
        return "dateDesc"
    }
    if(selectedOptions.innerText == "Ближайшие даты"){
        return "dateAsc"
    }
    if (selectedOptions.innerText == "Дальние даты"){
        return "dateDesc"
    }

}

const getNearFarDates = () => {
    const selectedOptions = selectDate.options[selectDate.selectedIndex]
    console.log(selectedOptions.innerText)
    if(selectedOptions.innerText == "Ближайшие даты"){
        return "dateAsc"
    }
    if (selectedOptions.innerText == "Дальние даты"){
        return "dateDesk"
    }

}



const getTrainer = () => {


}

//


selectDate.addEventListener("change", ()=>{
    blockOfTrainings.innerText = ""
    dateAscDateDesk = getNearFarDates()
    showPages(currentPage , dateAscDateDesk)

})

selectBranch.addEventListener("change" , ()=>{
    blockOfTrainings.innerText = ""

})
// const blockOfTrainings = document.getElementById("training-list")
// const nextButton = document.getElementById("next-button")
// const prevButton = document.getElementById("prev-button")
// let currentPage = 0


nextButton.addEventListener("click",()=>{
    blockOfTrainings.innerText = ""


    const trainings = getArray(currentPage + 1)
    console.log(trainings)
    if (trainings.length == 0){
        nextButton.style.display="none"
    }else{
        currentPage++
        console.log(currentPage)
        console.log(getNearFarDates())
        showPages(currentPage ,dateAscDateDesk)
    }

})

prevButton.addEventListener("click" , ()=>{
    blockOfTrainings.innerText = ""


    if(currentPage == 0){
        currentPage = 0
        console.log(currentPage)
    }else{
        currentPage--
        console.log(currentPage)
    }


    showPages(currentPage ,dateAscDateDesk)
    nextButton.style.display="block"

})






const showPages = async(currentPage ,dateAscDateDesk) =>{
    const trainings = await getData(currentPage ,dateAscDateDesk)
    trainings.forEach(train => {
        const card = createTraining(train)
        blockOfTrainings.append(card)
    })


}

const getArray = async(currentPage) =>{
    const trainings =  await fetch(`http://localhost:8080/trainings?page=${currentPage}&size=5`)
    const data = await trainings.json()
    return data
}

const createTraining = train => {
    const card = document.createElement("div")
    const topic = document.createElement("h2")
    const trainer = document.createElement("p")
    const date = document.createElement("p")
    const buttonOpenDescription = document.createElement("button")
    const buttonReg = document.createElement("button")
    const trainDetails = document.createElement("div")
    const description = document.createElement("p")

    card.className = "training-item"
    buttonOpenDescription.className = "btn btn-info"
    buttonReg.className = "btn btn-primary"

    topic.innerText = train.topic
    trainer.innerText = train.trainerName
    date.innerText = train.dateTime
    description.innerText = train.description
    description.style.display = "display:none"

    card.append(topic)
    card.append(trainer)
    card.append(date)
    card.append(buttonOpenDescription)
    card.append(buttonReg)
    card.append(trainDetails)
    card.append(description)

    description.style.display = "none"
    let showKey = true

    buttonOpenDescription.addEventListener("click", () => {
        if (showKey) {
            description.style.display = "block"
            showKey = false
        } else {
            description.style.display = "none"
            showKey = true
        }

    })

    return card


}



showPages(currentPage ,dateAscDateDesk)