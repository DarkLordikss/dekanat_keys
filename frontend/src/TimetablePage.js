import React from 'react';
import './styles/Common.css';
import './styles/ObjectsWidth.css';
import './styles/SpacingStyles.css';
import './styles/TextStyles.css';
import { checkAuth } from "./Connector.js";

class TimetablePage extends React.Component {
    componentDidMount() {
        let authorized = checkAuth();
        if (!authorized) {
            window.location.href = '/';
        }
    }
  
    render() {
      return (
        <div class="container content-center">
          <div class="middle-wall-object no-wrap">
            <div class="raw-object times-box nullable-object vertical-top">
              <div class="raw-object w100-obj nullable-object content-left padding-h-big">
                  <div class="raw-object w100-obj nullable-object text-width-bold text-invis text-big">ДН</div>
                  <div class="raw-object w100-obj nullable-object text-width-normal text-invis text-small">Да</div>
              </div><div class="raw-object nullable-object w100-obj content-right padding-h-big day-box">
                <div class="raw-object nullable-object w100-obj text-width-bold text-blue text-big ">8:45</div>
                <div class="raw-object nullable-object w100-obj text-width-normal text-light text-small ">10:20</div>
              </div><div class="raw-object nullable-object w100-obj content-right padding-h-big day-box">
                <div class="raw-object nullable-object w100-obj text-width-bold text-blue text-big ">10:35</div>
                <div class="raw-object nullable-object w100-obj text-width-normal text-light text-small ">12:10</div>
              </div><div class="raw-object nullable-object w100-obj content-right padding-h-big day-box">
                <div class="raw-object nullable-object w100-obj text-width-bold text-blue text-big ">12:25</div>
                <div class="raw-object nullable-object w100-obj text-width-normal text-light text-small ">14:00</div>
              </div><div class="raw-object nullable-object w100-obj content-right padding-h-big day-box">
                <div class="raw-object nullable-object w100-obj text-width-bold text-blue text-big ">14:45</div>
                <div class="raw-object nullable-object w100-obj text-width-normal text-light text-small ">16:20</div>
              </div><div class="raw-object nullable-object w100-obj content-right padding-h-big day-box">
                <div class="raw-object nullable-object w100-obj text-width-bold text-blue text-big ">16:35</div>
                <div class="raw-object nullable-object w100-obj text-width-normal text-light text-small ">18:10</div>
              </div><div class="raw-object nullable-object w100-obj content-right padding-h-big day-box">
                <div class="raw-object nullable-object w100-obj text-width-bold text-blue text-big ">18:25</div>
                <div class="raw-object nullable-object w100-obj text-width-normal text-light text-small ">20:00</div>
              </div>
            </div><div class="raw-object days-box nullable-object">
              <div class="w100-obj no-wrap nullable-object">
                <div class="raw-object w12_5-obj smaller-object">
                  <div class="raw-object w100-obj nullable-object content-left">
                    <div class="raw-object w100-obj nullable-object text-width-bold text-blue text-big">Понедельник</div>
                    <div class="raw-object w100-obj nullable-object text-width-normal text-light text-small">12 фев</div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div>
                </div><div class="raw-object w12_5-obj smaller-object">
                  <div class="raw-object w100-obj nullable-object content-left">
                    <div class="raw-object w100-obj nullable-object text-width-bold text-blue text-big">Вторник</div>
                    <div class="raw-object w100-obj nullable-object text-width-normal text-light text-small">13 фев</div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div>
                </div><div class="raw-object w12_5-obj smaller-object">
                  <div class="raw-object w100-obj nullable-object content-left">
                    <div class="raw-object w100-obj nullable-object text-width-bold text-blue text-big">Среда</div>
                    <div class="raw-object w100-obj nullable-object text-width-normal text-light text-small">14 фев</div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div>
                </div><div class="raw-object w12_5-obj smaller-object">
                  <div class="raw-object w100-obj nullable-object content-left">
                    <div class="raw-object w100-obj nullable-object text-width-bold text-blue text-big">Четверг</div>
                    <div class="raw-object w100-obj nullable-object text-width-normal text-light text-small">15 фев</div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div>
                </div><div class="raw-object w12_5-obj smaller-object">
                  <div class="raw-object w100-obj nullable-object content-left">
                    <div class="raw-object w100-obj nullable-object text-width-bold text-blue text-big">Пятница</div>
                    <div class="raw-object w100-obj nullable-object text-width-normal text-light text-small">16 фев</div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div>
                </div><div class="raw-object w12_5-obj smaller-object">
                  <div class="raw-object w100-obj nullable-object content-left">
                    <div class="raw-object w100-obj nullable-object text-width-bold text-blue text-big">Суббота</div>
                    <div class="raw-object w100-obj nullable-object text-width-normal text-light text-small">17 фев</div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div>
                </div><div class="raw-object w12_5-obj smaller-object">
                  <div class="raw-object w100-obj nullable-object content-left">
                    <div class="raw-object w100-obj nullable-object text-width-bold text-blue text-big">Воскресенье</div>
                    <div class="raw-object w100-obj nullable-object text-width-normal text-light text-small">18 фев</div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                    <div class="raw-object raw-box nullable-object filling-box"></div>
                  </div>
                </div>
              </div>
              
            </div>
          </div>
        </div>
      );
    }
  }

export default TimetablePage;