import React from 'react';
import './styles/Common.css';
import './styles/ObjectsWidth.css';
import './styles/SpacingStyles.css';
import './styles/TextStyles.css';
import './styles/ButtonsStyles.css';
import './styles/HeaderStyles.css';

class HeaderModule extends React.Component {
    async componentDidMount() {
        document.addEventListener('click', this.handleClick);
    }

    handleClick = async (event) => {
        const target = event.target;
        const targetId = target.id;
        const targetClasses = target.classList;
    
        if (targetClasses.contains('navigation-bar')) {
          window.location.href = targetId;
        }
    };

    render() {
        return (
        <div class='header vertical-middle content-center'>
            <div class='raw-object bigger-object w100-obj content-left navigation-container vertical-middle'>
                <div id="/timetable" class='raw-object nullable-object navigation-bar content-center vertical-middle text-big text-width-bold text-white padding-v-b-small margin-h-normal'>
                    Расписание
                </div><div id="/users" class='raw-object nullable-object navigation-bar content-center vertical-middle text-big text-width-bold text-white padding-v-b-small margin-h-normal'>
                    Пользователи
                </div>
            </div>
        </div>
        );
    }
}

export default HeaderModule;