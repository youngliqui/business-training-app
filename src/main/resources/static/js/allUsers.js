
const getUsers = async () => {
    const basicAuthToken = localStorage.getItem('basicAuthToken')
    if (basicAuthToken) {
        const response = await fetch('http://localhost:8080/users', {
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

const deleteFun = async (userId) => {
    const basicAuthToken = localStorage.getItem('basicAuthToken')
    if (basicAuthToken) {
        const response = await fetch(`http://localhost:8080/users/${userId}`, {
            method: "DELETE",
            headers: {
                'Authorization': `Basic ${basicAuthToken}`
            }
        })
    }
}

const disableFun = async (userId) => {
    const basicAuthToken = localStorage.getItem('basicAuthToken')
    if (basicAuthToken) {
        const response = await fetch(`http://localhost:8080/users/${userId}/block`, {
            method: "PATCH",
            headers: {
                'Authorization': `Basic ${basicAuthToken}`
            }
        })
    }

}


const createUser = (user) => {
    const trEl = document.createElement("tr")
    const tdName = document.createElement("td")
    const tdEmail = document.createElement("td")
    const tdRole = document.createElement("td")
    const tdButtons = document.createElement("td")
    const buttonDisable = document.createElement("button")
    const buttonRemove = document.createElement("button")

    buttonDisable.className = "btn btn-primary"
    buttonRemove.className = "btn btn-danger"

    buttonDisable.innerText = "Заблокировать"
    buttonRemove.innerText = "Удалить"



    tdName.innerText = user.name
    tdEmail.innerText = user.email
    tdRole.innerText = user.role

    tdButtons.appendChild(buttonDisable)
    tdButtons.appendChild(buttonRemove)
    trEl.appendChild(tdName)
    trEl.appendChild(tdEmail)
    trEl.appendChild(tdRole)
    trEl.appendChild(tdButtons)



    buttonDisable.addEventListener("click", () => {
        disableFun(user.id)
        window.location.reload()
    })

    buttonRemove.addEventListener("click", () => {
        deleteFun(user.id)
        window.location.reload()
    })

    return trEl

}




const showUsers = async () => {

    const users = await getUsers()
    users.forEach(user => {
        const trUser = createUser(user)
        document.querySelector(".users").append(trUser)

    });


}




showUsers()
