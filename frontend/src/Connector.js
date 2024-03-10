import { getStatusString, parseDate, getBarriers, parseSmallDate, getStatusStyle} from "./Parsers.js";import {useState} from "react";

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
            else if (response.status === 403) {
                window.location.href = '/'
            }
            return null;
        }
        const data = await response.json();
        
        return data;
    } catch (error) {
        console.error('Error:', error.message);
    }
}

export async function getTimetable(start_date = null, end_date = null, building = 2, classrooms = [402], statuses = [1, 2, 3, 4, 5, 6], user_id = null) {
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
                'Content-Type': 'application/json'
            },
        });
        

        if (!response.ok) {
            if (response.status === 400) {
                alert('Произошла ошибка!\nПроверьте правильность заполнения полей')
            }
            else if (response.status === 500) {
                alert('Произошла ошибка!\nОшибка на сервере!')
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

export async function getRooms() {
    try {
        const response = await fetch(`${default_way}classroom/all`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            },
        });
        

        if (!response.ok) {
            if (response.status === 400) {
                alert('Произошла ошибка!\nПроверьте правильность заполнения полей')
            }
            else if (response.status === 500) {
                alert('Произошла ошибка!\nОшибка на сервере!')
            }
            return null;
        }
        const data = await response.json();
        
        return data;
    } catch (error) {
        console.error('Error:', error.message);
    }
}

export async function getBuildings() {
    try {
        const response = await fetch(`${default_way}building/get-all-buildings`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            },
        });
        

        if (!response.ok) {
            if (response.status === 400) {
                alert('Произошла ошибка!\nПроверьте правильность заполнения полей')
            }
            else if (response.status === 500) {
                alert('Произошла ошибка!\nОшибка на сервере!')
            }
            return null;
        }
        const data = await response.json();
        
        return data;
    } catch (error) {
        console.error('Error:', error.message);
    }
}


export async function getRoomsFromBuilding(building) {
    try {
        const response = await fetch(`${default_way}building/get-classrooms-from-building?building=${building}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            },
        });
        

        if (!response.ok) {
            if (response.status === 400) {
                alert('Произошла ошибка!\nПроверьте правильность заполнения полей')
            }
            else if (response.status === 500) {
                alert('Произошла ошибка!\nОшибка на сервере!')
            }
            return null;
        }
        const data = await response.json();
        
        return data;
    } catch (error) {
        console.error('Error:', error.message);
    }
}

export async function changeApplicationStatus(app_id, status_id, token) {
    try {
        const response = await fetch(`${default_way}applications/change_deal_status/${app_id}?status_id=${status_id}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
        });
        

        if (!response.ok) {
            if (response.status === 400) {
                alert(`Произошла ошибка!\nНевозможно сменить статус с текущего на "${await getStatusString(status_id)}"`)
            }
            else if (response.status === 403) {
                window.location.href = '/'
            }
            else if (response.status === 500) {
                alert('Произошла ошибка!\nОшибка на сервере!')
            }
            return null;
        }
        const data = await response.json();
        
        return data;
    } catch (error) {
        console.error('Error:', error.message);
    }
}

export async function getUsers(user, token) {
    try {
        const response = await fetch(`${default_way}user/users?roles=1&roles=2`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
        });
        console.log(response)
        if (!response.ok) {
            if (response.status === 400) {
                alert(`Произошла ошибка!\nСписок пуст"`)
            }
            else if (response.status === 403) {
                window.location.href = '/'
            }
            else if (response.status === 500) {
                alert('Произошла ошибка!\nОшибка на сервере!')
            }
            return null;
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error:', error.message);
    }
}

export const updateRole = async (id, role, selectedValue) => {
    try {
        await fetch(`${default_way}user/change_role/?another_user_id=${id}&wished_role_id=${selectedValue}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem("keyGuardUserToken")}`,
            },
        });

    } catch (error) {
        console.error('Ошибка:', error);
    }
}