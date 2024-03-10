import React from 'react';
import './styles/Common.css';
import './styles/ObjectsWidth.css';
import './styles/SpacingStyles.css';
import './styles/TextStyles.css';
import './styles/ButtonsStyles.css';

class KeysPage extends React.Component {
    async componentDidMount() {
        document.addEventListener('click', this.handleClick);
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
                <div class="raw-object w100-obj content-left text-width-normal text-normal">

                    <div class="raw-object w50-obj padding-h-normal margin-v-small" id="key">
                        <div class="raw-object nullable-object w20-small-obj raw-box box-borders radius-l padding-h-normal content-center no-radius" id="key_number">
                            200
                        </div><div class="raw-object nullable-object w80-small-obj raw-box box-borders radius-r padding-h-normal content-center no-radius" id="key_status">
                            Свободен
                        </div>
                    </div><div class="raw-object w50-obj padding-h-normal margin-v-small" id="key">
                        <div class="raw-object nullable-object w20-small-obj raw-box box-borders radius-l padding-h-normal content-center no-radius" id="key_number">
                            200
                        </div><div class="raw-object nullable-object w80-small-obj raw-box box-borders radius-r padding-h-normal content-center no-radius" id="key_status">
                            Свободен
                        </div>
                    </div><div class="raw-object w50-obj padding-h-normal margin-v-small" id="key">
                        <div class="raw-object nullable-object w20-small-obj raw-box box-borders radius-l padding-h-normal content-center no-radius" id="key_number">
                            200
                        </div><div class="raw-object nullable-object w80-small-obj raw-box box-borders radius-r padding-h-normal content-center no-radius" id="key_status">
                            Свободен
                        </div>
                    </div><div class="raw-object w50-obj padding-h-normal margin-v-small" id="key">
                        <div class="raw-object nullable-object w20-small-obj raw-box box-borders radius-l padding-h-normal content-center no-radius" id="key_number">
                            200
                        </div><div class="raw-object nullable-object w80-small-obj raw-box box-borders radius-r padding-h-normal content-center no-radius" id="key_status">
                            Свободен
                        </div>
                    </div><div class="raw-object w50-obj padding-h-normal margin-v-small" id="key">
                        <div class="raw-object nullable-object w20-small-obj raw-box box-borders radius-l padding-h-normal content-center no-radius" id="key_number">
                            200
                        </div><div class="raw-object nullable-object w80-small-obj raw-box box-borders radius-r padding-h-normal content-center no-radius" id="key_status">
                            Свободен
                        </div>
                    </div><div class="raw-object w50-obj padding-h-normal margin-v-small" id="key">
                        <div class="raw-object nullable-object w20-small-obj raw-box box-borders radius-l padding-h-normal content-center no-radius" id="key_number">
                            200
                        </div><div class="raw-object nullable-object w80-small-obj raw-box box-borders radius-r padding-h-normal content-center no-radius" id="key_status">
                            Свободен
                        </div>
                    </div><div class="raw-object w50-obj padding-h-normal margin-v-small" id="key">
                        <div class="raw-object nullable-object w20-small-obj raw-box box-borders radius-l padding-h-normal content-center no-radius" id="key_number">
                            200
                        </div><div class="raw-object nullable-object w80-small-obj raw-box box-borders radius-r padding-h-normal content-center no-radius" id="key_status">
                            Свободен
                        </div>
                    </div>
                    
                    

                </div>
            </div>
        </div>
        );
    }
}

export default KeysPage;