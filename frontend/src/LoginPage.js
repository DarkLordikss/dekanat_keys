import React from 'react';
import './common.css';
import { login } from "./Connector.js";

class LoginPage extends React.Component {
    handleLoginClick = async () => {
      let email = document.getElementById('field_email').value;
      let password = document.getElementById('field_password').value;
      let res = await login(email, password);
      if (res != null) {
        localStorage.setItem('keyGuardUserToken', res.access_token);
        window.location.href = '/main';
      }
    };
  
    render() {
      return (
        <div>
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
        </div>
      );
    }
  }

export default LoginPage;