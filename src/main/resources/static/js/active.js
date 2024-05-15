const getActiveTrainings = async () => {
    const basicAuthToken = localStorage.getItem('basicAuthToken')
    if (basicAuthToken) {
        const response = await fetch(`http://localhost:8080/users/trainings/active`, {
            headers: {
                'Authorization': `Basic ${basicAuthToken}`
            }
        })
        if (response.ok) {
            return response.json()
        }
    }
}


const createActiveTraining = (train) => {
    const trainEl = document.createElement("div")
    const trainName = document.createElement("h2")
    const trainDate = document.createElement("p")
    const buttonInfo = document.createElement("button")
    const trainDesDiv = document.createElement("div")
    const trainDesP = document.createElement("p")
    const buttonRate = document.createElement("button")
    const buttonUnload = document.createElement("button")
    const materialsText = document.createElement("p")
    const homeworksText = document.createElement("p")
    const divMaterials = document.createElement("div")
    const divHomeworks = document.createElement("div")
    materialsText.innerText = "Материалы:"
    homeworksText.innerText = "Домашняя работа:"







    createMaterials(train.id).then(divM => {
        createMaterials(train.id).then(divF => {
            divM.className = "materials"
            divF.className = "files"
            divMaterials.appendChild(materialsText)
            divMaterials.appendChild(divF)
            trainDesDiv.appendChild(divMaterials)
        })
    })





    createHomeworks(train.id).then(divM => {
        createHomeworks(train.id).then(divF => {
            divHomeworks.className = "block-materials"
            divM.className = "homeworks"
            divF.className = "files"
            divHomeworks.appendChild(homeworksText)
            divHomeworks.appendChild(divF)
            trainDesDiv.appendChild(divHomeworks)
            trainDesDiv.appendChild(buttonUnload)
        })
    })

    trainEl.className = "training-item"
    buttonInfo.className = "btn-info"
    buttonUnload.className = "buttonUnload"
    buttonRate.className = "btn-primary"
    trainDesDiv.className = "training-details"

    trainDesDiv.style.display = "none"
    trainName.innerText = train.topic
    trainDate.innerText = train.dateTime
    trainDesP.innerText = train.description
    buttonUnload.innerText = "выгрузить домашнюю работу"
    buttonInfo.innerText = "подробнее"
    buttonRate.innerText = "оценить"




    trainEl.appendChild(trainName)
    trainEl.appendChild(trainDate)
    trainEl.appendChild(buttonInfo)
    trainEl.appendChild(buttonRate)
    trainEl.appendChild(trainDesDiv)
    trainDesDiv.appendChild(trainDesP)
    trainDesDiv.appendChild(divMaterials)

    trainDesDiv.appendChild(divHomeworks)
    trainDesDiv.appendChild(homeworksText)


    let keyShow = true
    buttonInfo.addEventListener("click", () => {
        if (keyShow) {
            trainDesDiv.style.display = "block"
            keyShow = false
        } else {
            trainDesDiv.style.display = "none"
            keyShow = true
        }
    })


    return trainEl
}


const showActiveTrainings = async () => {
    const trainings = await getActiveTrainings()
    trainings.forEach(train => {
        const el = createActiveTraining(train)
        document.querySelector(".training-list").appendChild(el)
    });
}

const getMaterials = async (id) => {
    const basicAuthToken = localStorage.getItem('basicAuthToken');
    if (basicAuthToken) {
        const response = await fetch(`http://localhost:8080/trainings/${id}/materials`, {
            headers: {
                'Authorization': `Basic ${basicAuthToken}`
            }
        });
        if (response.ok) {
            return await response.json();
        }
    }
}


const getHomeworks = async (id) => {
    const basicAuthToken = localStorage.getItem('basicAuthToken');
    if (basicAuthToken) {
        const response = await fetch(`http://localhost:8080/trainings/${id}/homeworks`, {
            headers: {
                'Authorization': `Basic ${basicAuthToken}`
            }
        });
        if (response.ok) {
            return await response.json();
        }
    }
}




const createMaterials = async (id) => {
    const links = await getMaterials(id);
    const linksDiv = document.createElement("div");
    links.forEach(link => {
        const aEl = document.createElement("a");
        aEl.innerText = link.filename;
        aEl.href = link.link;
        linksDiv.appendChild(aEl);
    });
    return linksDiv;
}

const createHomeworks = async (id) => {
    const links = await getHomeworks(id);
    const linksDiv = document.createElement("div");
    links.forEach(link => {
        const aEl = document.createElement("a");
        aEl.innerText = link.filename;
        aEl.href = link.link;
        linksDiv.appendChild(aEl);
    });
    return linksDiv;
}


showActiveTrainings()
