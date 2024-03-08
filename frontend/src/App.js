import React from 'react';
import LoginPage from './LoginPage';
import TimetablePage from './TimetablePage';
import HeaderModule from './HeaderModule';

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