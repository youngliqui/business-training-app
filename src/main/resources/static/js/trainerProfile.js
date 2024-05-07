const trainingsListElement = document.querySelector(".trainer-trainings")

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


const setProfile = async () => {
    const profile = await getProfile()
    const response = await fetch(profile.imageLink)
    const blob = await response.blob()
    const imageUrl = URL.createObjectURL(blob)
    const image = document.querySelector(".profile-photo")
    const trainerName = profile.username
    const rating = profile.rating
    const trainingsList = profile.trainingArchive
    image.src = imageUrl





    document.querySelector(".trainer-name").innerText = trainerName
    document.querySelector(".user-rating").innerText = rating
    showTrainings(trainingsList)
}



getProfile()
setProfile()
