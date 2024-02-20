const default_way = 'https://zomnbi-mozgi-kushat-iii-backend.pmc-python.ru/api/v1/';

export async function login(email, password) {
    let data_body = JSON.stringify({
        "email": email,
        "password": password
    });
    try {
        const response = await fetch(`${default_way}user/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: data_body
        });
        

        if (!response.ok) {
            if (response.status === 400) {
                alert('Произошла ошибка при входе!\nПроверьте правильность заполнения полей')
            }
            else if (response.status === 500) {
                alert('Произошла ошибка при входе!\nОшибка на сервере!')
            }
            return null;
        }
        const data = await response.json();
        
        return data;
    } catch (error) {
        console.error('Error:', error.message);
    }
}

export async function user(token) {
    try {
        const response = await fetch(`${default_way}user`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            }
        });
        

        if (!response.ok) {
            if (response.status === 500) {
                alert('Произошла ошибка при входе!\nОшибка на сервере!')
            }
            return null;
        }
        const data = await response.json();
        
        return data;
    } catch (error) {
        console.error('Error:', error.message);
    }
}

export async function checkAuth() {
    let userToken = localStorage.getItem('keyGuardUserToken');
    let res = await user(userToken);
    if (res == null) {
        return false;
    }
    if (res.role === 'Сотрудник деканата') {
        return true;
    }
    return false;
}
