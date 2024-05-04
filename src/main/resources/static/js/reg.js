const nameInput = document.getElementById("username");
const emailInput = document.getElementById("email");
const passwordInput = document.getElementById("password");
const sendButton = document.querySelector(".btn.auth-btn");
const regForm = document.querySelector(".registration-form");
const url = "http://localhost:8080/users/new-user";
const userInput = document.getElementById("user");
const adminInput = document.getElementById("trainer");

let defaultRole = "user";



userInput.addEventListener("click", (e) => {
    e.preventDefault()
    userInput.style.backgroundColor = "green"
    adminInput.style.backgroundColor = "white"
    defaultRole = userInput.innerText.toLowerCase()
})

adminInput.addEventListener("click", (e) => {
    e.preventDefault()
    userInput.style.backgroundColor = "white"
    adminInput.style.backgroundColor = "green"
    defaultRole = adminInput.innerText.toLowerCase()
})

// Отправка запроса
const sendInfo = async (currentRole) => {
    if (!nameInput.value || !emailInput.value || !passwordInput.value) {
        alert("Пожалуйста, заполните все поля");
        return;
    }

    const data = JSON.stringify({
        name: nameInput.value,
        email: emailInput.value,
        password: passwordInput.value,
        role: currentRole
    });

    try {
        const response = await fetch(url, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: data
        });

        if (!response.ok) {
            throw new Error(`Ошибка по адресу ${url}, статус ошибки ${response.status}`);
        }

        const result = await response.json();
        console.log(result);
    } catch (error) {
        console.error(error);
    }
};

regForm.addEventListener("submit", (e) => {
    e.preventDefault();
    sendInfo(defaultRole);
});