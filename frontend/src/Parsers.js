const StatusEnum = {
    1: 'Не обработано',
    2: 'Подтверждено',
    3: 'Ключ получен',
    4: 'Ключ сдан',
    5: 'Отклонено',
    6: 'Недействительно'
};

const StatusStyleEnum = {
    1: 'button-default',
    2: 'button-good',
    3: 'button-warning',
    4: 'button-gifted',
    5: 'button-wrong',
    6: 'button-inactive'
};

export const MonthEnum = {
    "01": 'янв',
    "02": 'фев',
    "03": 'мар',
    "04": 'апр',
    "05": 'май',
    "06": 'июн',
    "07": 'июл',
    "08": 'авг',
    "09": 'сен',
    "10": 'окт',
    "11": 'ноя',
    "12": 'дек'
};

// Функция для преобразования числового значения в строковое представление
export const getStatusString = async (statusValue) => {
    return StatusEnum[statusValue] || 'Неизвестно';
};

export const getStatusStyle = async (statusValue) => {
    return StatusStyleEnum[statusValue] || 'button-default';
};

export const parseDate = async (date) => {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');

    const formattedDate = `${year}-${month}-${day}`;
    return formattedDate;
}

export const getBarriers = async (date) => {
    const day = date.getDay(); // Получаем номер дня недели (0 - воскресенье, 1 - понедельник, ..., 6 - суббота)
    const diff = date.getDate() - day + 1; // Вычисляем разницу между текущим днем и понедельником
    let mondayDate = new Date(date.setDate(diff));
    let sundayDate = new Date(date.setDate(diff + 6));
    return {"monday": mondayDate, "sunday": sundayDate};
}

export const parseSmallDate = async (date) => {
    let distructed = date.split('-');
    let month = distructed[1];
    let day = distructed[2];

    return `${day} ${MonthEnum[month]}`;
}