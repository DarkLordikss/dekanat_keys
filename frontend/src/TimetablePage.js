import React from 'react';
import './styles/Common.css';
import './styles/ObjectsWidth.css';
import './styles/SpacingStyles.css';
import './styles/TextStyles.css';
import './styles/ButtonsStyles.css';
import { checkAuth, getTimetable } from "./Connector.js";
import { getStatusString, parseDate, getBarriers, parseSmallDate, getStatusStyle} from "./Parsers.js";

class TimetablePage extends React.Component {
  constructor() {
    super();
    this.state = {
      scheduleSet: false
    };
  }

  async componentDidMount() {
    let authorized = checkAuth();
    if (!authorized) {
        window.location.href = '/';
    }

    let start_end = await getBarriers(new Date());
    let res_tt = await getTimetable(localStorage.getItem("keyGuardUserToken"), "2", await parseDate(start_end.monday), await parseDate(start_end.sunday), [402], [1, 2, 3, 4, 5]);

    if (!this.state.scheduleSet) {
      await this.setSchedule(res_tt.schedule);
      this.setState({ scheduleSet: true });
    }
  }

  async setSchedule(schedule) {
    for (let i = 0; i < 7; i++) {
      const day = schedule[i];
      let curDate = await parseSmallDate(day.date);
      document.getElementById(`date_${i}`).innerText = curDate;
      let timetable = day.timetable;
      for (let j = 1; j < 7; j++) {
        const time = timetable[j] ? timetable[j] : [];
        for (let k = 0; k < time.length; k++) {
          let pare = time[k];
          document.getElementById(`day_box_${i}_${j}`).innerHTML += 
          `<div class="raw-object w100-obj nullable-object raw-box ${await getStatusStyle(pare.status)} margin-v-normal padding-h-normal padding-v-normal">
            <div class="raw-object w100-obj nullable-object text-big text-width-bold">${pare.name}</div>
            <div class="raw-object w100-obj nullable-object text-normal margin-v-b-normal">${pare.buildings} к., ${pare.class_number} ауд.</div>
            <div class="raw-object w100-obj nullable-object text-normal margin-v-b-normal">${await getStatusString(pare.status)}</div>
            <div class="raw-object w100-obj nullable-object text-small">Описание: ${pare.description}</div>
          </div>`
        }
      }
    }
    this.setState({ scheduleSet: true });
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
                  <div class="raw-object w100-obj nullable-object text-width-normal text-light text-small" id="date_0">12 фев</div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_0_1"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_0_2"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_0_3"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_0_4"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_0_5"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_0_6"></div>
                </div>
              </div><div class="raw-object w12_5-obj smaller-object">
                <div class="raw-object w100-obj nullable-object content-left">
                  <div class="raw-object w100-obj nullable-object text-width-bold text-blue text-big">Вторник</div>
                  <div class="raw-object w100-obj nullable-object text-width-normal text-light text-small" id="date_1">13 фев</div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_1_1"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_1_2"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_1_3"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_1_4"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_1_5"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_1_6"></div>
                </div>
              </div><div class="raw-object w12_5-obj smaller-object">
                <div class="raw-object w100-obj nullable-object content-left">
                  <div class="raw-object w100-obj nullable-object text-width-bold text-blue text-big">Среда</div>
                  <div class="raw-object w100-obj nullable-object text-width-normal text-light text-small" id="date_2">14 фев</div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_2_1"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_2_2"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_2_3"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_2_4"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_2_5"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_2_6"></div>
                </div>
              </div><div class="raw-object w12_5-obj smaller-object">
                <div class="raw-object w100-obj nullable-object content-left">
                  <div class="raw-object w100-obj nullable-object text-width-bold text-blue text-big">Четверг</div>
                  <div class="raw-object w100-obj nullable-object text-width-normal text-light text-small" id="date_3">15 фев</div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_3_1"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_3_2"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_3_3"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_3_4"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_3_5"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_3_6"></div>
                </div>
              </div><div class="raw-object w12_5-obj smaller-object">
                <div class="raw-object w100-obj nullable-object content-left">
                  <div class="raw-object w100-obj nullable-object text-width-bold text-blue text-big">Пятница</div>
                  <div class="raw-object w100-obj nullable-object text-width-normal text-light text-small" id="date_4">16 фев</div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_4_1"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_4_2"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_4_3"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_4_4"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box" id="day_box_4_5"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_4_6"></div>
                </div>
              </div><div class="raw-object w12_5-obj smaller-object">
                <div class="raw-object w100-obj nullable-object content-left">
                  <div class="raw-object w100-obj nullable-object text-width-bold text-blue text-big">Суббота</div>
                  <div class="raw-object w100-obj nullable-object text-width-normal text-light text-small" id="date_5">17 фев</div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_5_1"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_5_2"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_5_3"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_5_4"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_5_5"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_5_6"></div>
                </div>
              </div><div class="raw-object w12_5-obj smaller-object">
                <div class="raw-object w100-obj nullable-object content-left">
                  <div class="raw-object w100-obj nullable-object text-width-bold text-blue text-big">Воскресенье</div>
                  <div class="raw-object w100-obj nullable-object text-width-normal text-light text-small" id="date_6">18 фев</div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_6_1"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_6_2"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_6_3"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_6_4"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_6_5"></div>
                </div><div class="raw-object w100-obj nullable-object content-left padding-h-r-normal padding-v-normal vertical-top day-box">
                  <div class="raw-object raw-box nullable-object filling-box scrollable-object padding-h-small" id="day_box_6_6"></div>
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