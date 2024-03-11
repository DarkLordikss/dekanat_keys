import React from 'react';
import './styles/Common.css';
import './styles/ObjectsWidth.css';
import './styles/SpacingStyles.css';
import './styles/TextStyles.css';
import './styles/ButtonsStyles.css';
import { getKeys, user } from "./Connector.js";

class KeysPage extends React.Component {
    async componentDidMount() {
        document.addEventListener('click', this.handleClick);
        
        await user(localStorage.getItem('keyGuardUserToken'));
        await this.generateKeys();
    }

    async generateKeys() {
        let keys = await getKeys();
        for (let i = 0; i < keys.length; i++) {
            const cur_key = keys[i];
            document.getElementById('keys_list').innerHTML += `<div class="raw-object w50-obj padding-h-normal margin-v-small" id="key_${cur_key.classroom_id}">
                    <div class="raw-object cell-obj nullable-object w30-small-obj raw-box box-borders radius-l padding-h-normal content-center no-radius" id="key_number">
                        к. ${cur_key.buildings}, ауд. ${cur_key.class_number}
                    </div><div class="${cur_key.user_id != null ? "button-wrong" : "button-good"} cell-obj raw-object nullable-object w70-small-obj raw-box box-borders radius-r padding-h-normal content-center no-radius" id="key_status">
                        ${cur_key.user_id != null ? "У пользователя: "+cur_key.name : "Свободен"}
                    </div>
                </div>`
        }
    }

    handleClick = async (event) => {
        const target = event.target;
        const targetId = target.id;
        const targetClasses = target.classList;
        
    };

    render() {
        return (
        <div class="container content-center">
            <div class="middle-wall-object">
                <div id="keys_list" class="raw-object w100-obj content-left text-width-normal text-normal">
                </div>
            </div>
        </div>
        );
    }
}

export default KeysPage;