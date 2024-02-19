import React from 'react';
import LoginPage from './LoginPage';
import MainPage from './MainPage';

function App() {
  // Получаем текущий путь из URL
  const currentPath = window.location.pathname;

  // Определяем компонент, который должен отображаться на текущем пути
  let currentComponent;
  if (currentPath === '/') {
    currentComponent = <LoginPage />;
  } else if (currentPath === '/main') {
    currentComponent = <MainPage />;
  } else {
    // Если путь не соответствует ни одному известному, можно отобразить страницу 404
    currentComponent = <div>404 Not Found</div>;
  }

  return (
    <div>
      {currentComponent}
    </div>
  );
}

export default App;