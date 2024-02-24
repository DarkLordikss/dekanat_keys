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

export async function getTimetable(token, building = 2, start_date = null, end_date = null, classrooms = [229, 402], statuses = [1, 2, 3, 4, 5], user_id = null) {
    const params = new URLSearchParams();
    params.append('building', building);
    params.append('start_date', start_date);

    for (let i = 0; i < classrooms.length; i++) {
        params.append('classrooms', classrooms[i]);
    }
    for (let i = 0; i < statuses.length; i++) {
        params.append('statuses', statuses[i]);
    }

    if (end_date != null) {
        params.append('end_date', end_date);
    }
    if (user_id) {
        params.append('user_id', user_id);
    }

    try {
        const response = await fetch(`${default_way}applications/show_with_status?${params.toString()}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
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
