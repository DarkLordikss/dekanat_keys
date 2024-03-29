import React, {useState} from 'react';
import LoginPage from './LoginPage';
import TimetablePage from './TimetablePage';
import HeaderModule from './HeaderModule';
import KeysPage from './KeysPage';

import UsersPage from './UsersPage';
function App() {
  // Получаем текущий путь из URL
  const currentPath = window.location.pathname;
  let header = <HeaderModule />;

  // Определяем компонент, который должен отображаться на текущем пути
  let currentComponent;
  if (currentPath === '/') {
    currentComponent = <LoginPage />;
    header = '';
  } else if (currentPath === '/timetable') {
    currentComponent = <TimetablePage />;
  } else if (currentPath === '/users') {
    import ('bootstrap/dist/css/bootstrap.min.css');
    currentComponent = <UsersPage />
  } else if (currentPath === '/keys') {
    currentComponent = <KeysPage />;
  } else {
    // Если путь не соответствует ни одному известному, можно отобразить страницу 404
    currentComponent = <div>404 Not Found</div>;
  }

  return (
    <div>
      {currentComponent}

      {header}
    </div>
  );
}

export default App;