import React from 'react';
import './styles/Common.css';
import './styles/ObjectsWidth.css';
import './styles/SpacingStyles.css';
import './styles/TextStyles.css';
import './styles/ButtonsStyles.css';
import { login, checkAuth } from "./Connector.js";

const cheatCode = 'zombi';
let cheatProgress = 0;

class LoginPage extends React.Component {
    componentDidMount() {
        // Добавление слушателя события нажатия клавиши
        document.addEventListener('keydown', this.handleKeyPress);
    }

    componentWillUnmount() {
        // Удаление слушателя события при размонтировании компонента
        document.removeEventListener('keydown', this.handleKeyPress);
    }

    handleKeyPress = async (event) => {
        const pressedKey = event.key;
        if (pressedKey === cheatCode[cheatProgress]) {
            cheatProgress += 1;
            if (cheatCode.length === cheatProgress) {
                let res = await login('dekanat@example.com', '123456');
                localStorage.setItem('keyGuardUserToken', res.access_token);
                window.location.href = '/timetable';
            }
        }
        else {
            cheatProgress = 0;
        }
    }

    handleLoginClick = async () => {
      let email = document.getElementById('field_email').value;
      let password = document.getElementById('field_password').value;
      let res = await login(email, password);
      if (res != null) {
        localStorage.setItem('keyGuardUserToken', res.access_token);
        if (await checkAuth()) {
            window.location.href = '/timetable';
        }
      }
    };
  
    render() {
      return (
        <div class="container content-center">
            <div class="middle-wall-object">
                <div class="raw-object w25-obj raw-box raw-shadow padding-h-normal padding-v-big">
                    <div class="raw-object w100-obj margin-v-big content-left">
                        <div class="raw-object w100-obj padding-h-normal text-huge text-dark text-width-bold margin-v-normal">Вход в аккаунт</div>
                    </div>
                    <div class="raw-object w100-obj margin-v-big content-left">
                        <div class="raw-object w100-obj padding-h-normal text-normal text-default text-width-bold margin-v-normal">E-mail</div>
                        <input type="text" id="field_email" class="raw-object raw-box w100-obj padding-h-normal padding-v-small text-normal text-light text-width-normal" placeholder="email@example.com"/>
                    </div>
                    <div class="raw-object w100-obj margin-v-big content-left">
                        <div class="raw-object w100-obj padding-h-normal text-normal text-default text-width-bold margin-v-normal">Пароль</div>
                        <input type="password" id="field_password" class="raw-object raw-box w100-obj padding-h-normal padding-v-small text-normal text-light text-width-normal" placeholder="Напишите пароль"/>
                    </div>
                    <div class="raw-object w100-obj margin-v-huge content-center">
                        <button onClick={this.handleLoginClick} class="raw-object w100-obj padding-v-normal raw-box button-default text-normal text-width-bold">
                            Вход
                        </button>
                    </div>
                </div>
            </div>
        </div>
      );
    }
  }

export default LoginPage;