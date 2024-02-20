import React from 'react';
import './common.css';
import { user } from "./Connector.js";

class MainPage extends React.Component {
    componentDidMount() {
        this.checkAuth();
    }

    async checkAuth() {
        let userToken = localStorage.getItem('keyGuardUserToken');
        let res = await user(userToken);
        if (res == null) {
            window.location.href = '/';
        }
    }
  
    render() {
      return (
        <div>
          <h2>Страница 1</h2>
        </div>
      );
    }
  }

export default MainPage;