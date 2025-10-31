
const url = 'http://localhost:8080';
<!--===-->
// Отримаємо поле serverResponse
const serverResponse = document.getElementById('server-response');
// Додаємо обробник події для кнопки реєстрації
document.getElementById('btn-registration').addEventListener('click', registerUser);
// Додаємо обробник події для кнопки залогінитися
document.getElementById('btn-login').addEventListener('click', loginUser);
// Додаємо обробник події для кнопки getUserDetails
document.getElementById('btn-user-details').addEventListener('click', getUserDetails);


<!--registration-->
function registerUser() {
    // Отримуємо значення з полів
    const username = document.getElementById('username-registration').value;
    const password = document.getElementById('password-registration').value;
    const email = document.getElementById('email').value;
    const yearsOld = document.getElementById('yearsOld').value;

    // Перевірка на порожні поля
    if (!username || !email || !password || !yearsOld) {
        serverResponse.textContent ='Будь ласка, заповніть всі поля.'
        return;
    }

    // Підготовка даних для відправки на сервер
    const registrationData = {
        username: username,
        password: password,
        email: email,
        yearsOld: yearsOld
    };

    // Виконання AJAX запиту для реєстрації користувача
    fetch(`${url}/auth/register`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(registrationData)
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(errorMessage => {
                    throw new Error(`Код помилки: ${response.status}. Повідомлення: ${errorMessage}`);
                });
            }
            return response.json();
        })
        .then(data => {
            if (data.token) {
                localStorage.setItem('token', data.token); // Зберігаємо токен у localStorage
                serverResponse.textContent = 'Реєстрація успішна! Токен: ' + data.token;
            } else {
                serverResponse.textContent = 'Помилка при реєстрації. Спробуйте ще раз.';
            }
        })
        .catch(error => {
            serverResponse.textContent = 'Помилка: ' + error.message; // Відображаємо текст помилки
        });
}

<!--login-->
function loginUser() {
    // Отримуємо значення з полів
    const username = document.getElementById('username-login').value;
    const password = document.getElementById('password-login').value;

    // Перевірка на порожні поля
    if (!username || !password) {
        serverResponse.textContent ='Будь ласка, заповніть всі поля.'
        return;
    }

    // Підготовка даних для відправки на сервер
    const loginData = {
        username: username,
        password: password
    };

    // Виконання AJAX запиту для залогінення користувача
    fetch(`${url}/auth/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(loginData)
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(errorMessage => {
                    throw new Error(`Код помилки: ${response.status}. Повідомлення: ${errorMessage}`);
                });
            }
            return response.json();
        })
        .then(data => {
            if (data.token) {
                localStorage.setItem('token', data.token); // Зберігаємо токен у localStorage
                serverResponse.textContent = 'залогінення успішне! Токен: ' + data.token;
            } else {
                serverResponse.textContent = 'Помилка при залогіненні. Спробуйте ще раз.';
            }
        })
        .catch(error => {
            serverResponse.textContent = 'Помилка: ' + error.message; // Відображаємо текст помилки
        });

}

<!--user-details-->
function getUserDetails() {
    // Отримуємо поля;
    const id = document.getElementById('id-user-details');
    const username = document.getElementById('username-user-details');
    const email = document.getElementById('email-user-details');
    const yearsOld = document.getElementById('yearsOld-user-details');

    // Отримати токен з локального сховища
    const token = localStorage.getItem('token');

    // Перевірка на наявність токену
    if (!token) {
    serverResponse.textContent = 'Токен відсутній. Будь ласка, залогіньтесь.';
    return;
}

    // Виконання AJAX запиту для userDetails користувача
    fetch(`${url}/users/get-user`, {
    method: 'GET',
    headers: {
    'Content-Type': 'application/json',
    // Додати токен у заголовок запиту
    'Authorization': `Bearer ${token}`
}
})
    .then(response => {
    if (!response.ok) {
    return response.text().then(errorMessage => {
    throw new Error(`Код помилки: ${response.status}. Повідомлення: ${errorMessage}`);
});
}
    return response.json();
})
    .then(data => {
    // Оновлення елементів з отриманими даними
    id.innerText = data.id;
    username.innerText = data.username;
    email.innerText = data.email;
    yearsOld.innerText = data.yearsOld;

})
    .catch(error => {
    serverResponse.textContent = 'Помилка: ' + error.message; // Відображаємо текст помилки
});
}

<!--refresh-token-->
(function playIntervalRefreshToken(){
    setInterval(() => {
        refreshToken()
    }, 1000 * 50 * 60 * 1)
})();

function  refreshToken(){
    // Отримати токен з локального сховища
    const token = localStorage.getItem('token');

    // Перевірка на наявність токену
    if (!token) {
        serverResponse.textContent = 'Токен не знайдено, будь ласка, увійдіть знову.';
        return;
    }

    // Виконання AJAX запиту для оновлення токену користувача
    fetch(`${url}/auth/refresh-token`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            // Додати токен у заголовок запиту
            'Authorization': `Bearer ${token}`
        }
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(errorMessage => {
                    throw new Error(`Код помилки: ${response.status}. Повідомлення: ${errorMessage}`);
                });
            }
            return response.json();
        })
        .then(data => {
            if (data.token) {
                localStorage.setItem('token', data.token); // Зберігаємо токен у localStorage
                serverResponse.textContent = 'оновлений Токен: ' + data.token;
            } else {
                serverResponse.textContent = 'Помилка при оновленні токену. Спробуйте ще раз.';
            }
        })
        .catch(error => {
            serverResponse.textContent = 'Помилка: ' + error.message; // Відображаємо текст помилки
        });
}

