const getData = async(url) =>{
    const basicAuthToken = localStorage.getItem('basicAuthToken');
    const response = await fetch ("http://localhost:8080/trainings", {
        headers: {
            'Authorization': `Basic ${basicAuthToken}`
        }
    })

    if(!response.ok){
        throw new Error(`error nahui ${response.status}`)

    }


    const data = await response.json()
    console.log(data)
    return data


}

getData()

const blockOfTrainings = document.getElementById("training-list")

const showTrainings = async() => {
    const trainings = await getData()
    console.log(trainings)
    trainings.forEach(train => {
        const card = createTraining(train)
        blockOfTrainings.append(card)
    })

}

showTrainings()




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


