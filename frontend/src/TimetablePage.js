import React from 'react';
import './styles/Common.css';
import './styles/ObjectsWidth.css';
import './styles/SpacingStyles.css';
import './styles/TextStyles.css';
import './styles/ButtonsStyles.css';
import { checkAuth, getTimetable, getBuildings, getRoomsFromBuilding, changeApplicationStatus } from "./Connector.js";
import { getStatusString, parseDate, getBarriers, parseSmallDate, getStatusStyle} from "./Parsers.js";

class TimetablePage extends React.Component {
  constructor() {
    super();
    this.state = {
      scheduleSet: false,
      buildings: [],
      classRooms: [],
      choosenRooms: [],
      weekDistance: 0,
      choosenApplicationId: '',
      choosen_day: [0, 0],
      applicatios: [[[], [], [], [], [], []], [[], [], [], [], [], []],
      [[], [], [], [], [], []],[[], [], [], [], [], []],
      [[], [], [], [], [], []], [[], [], [], [], [], []],
      [[], [], [], [], [], []]]
    };
  }

  async componentDidMount() {
    document.addEventListener('click', this.handleClick);
    document.getElementById('building_field').addEventListener('change', this.handleBuildingChange);
    document.getElementById('special_status').addEventListener('change', this.handleStatusChange);

    let authorized = checkAuth();
    if (!authorized) {
        window.location.href = '/';
    }

    let buildings = (await getBuildings()).buildings;
    for (let i = 0; i < buildings.length; i++) {
      let build = buildings[i]
      this.state.buildings.push(build);
      document.getElementById('building_field').innerHTML += `<option id='chooseBuilding_${build}' value='${build}'>${build}</option>`;
    }

    for (let i = 1; i <= 6; i++) {
      document.getElementById(`status_${i}_check`).checked = true;
      document.getElementById(`status_${i}_check`).addEventListener('change', this.handleStatusesFilter);
    }

    await this.generateClassrooms();
  }

  async generateClassrooms() {
    this.state.classRooms = [];
    this.state.choosenRooms = [];

    let rooms = (await getRoomsFromBuilding(document.getElementById('building_field').value)).classrooms;
    for (let i = 0; i < rooms.length; i++) {
      let room = rooms[i];
      this.state.classRooms.push(room);
      this.state.choosenRooms.push(room);
      document.getElementById('classrooms').innerHTML += `<div class="classroom-button raw-object nullable-object button-good auditory-object padding-h-big margin-h-r-small" id="${room.id}">${room.number}</div>`
    }

    await this.generateSchedule();
  }

  async generateSchedule() {
    let start_end = await getBarriers(new Date(), this.state.weekDistance);
    console.log(start_end);
    let res_tt;

    let choosenNumbersRooms = [];
    let choosenStatuses = [];

    for (let i = 0; i < this.state.choosenRooms.length; i++) {
      let number = this.state.choosenRooms[i].number;
      choosenNumbersRooms.push(number);
    }

    for (let i = 1; i <= 6; i++) {
      if (document.getElementById(`status_${i}_check`).checked) {
        choosenStatuses.push(i);
      }
    }
    
    res_tt = await getTimetable(await parseDate(start_end.monday), await parseDate(start_end.sunday), document.getElementById('building_field').value, choosenNumbersRooms, choosenStatuses);

    if (res_tt == null) {
      this.state.weekDistance = 0;
      res_tt = await getTimetable(await parseDate(start_end.monday), await parseDate(start_end.sunday));
    }
    
    await this.setSchedule(res_tt.schedule);
  }

  async setSchedule(schedule) {

    for (let i = 0; i < 7; i++) {
      const day = schedule[i];
      let curDate = await parseSmallDate(day.date);
      document.getElementById(`date_${i}`).innerText = curDate;
      let timetable = day.timetable;
      for (let j = 1; j < 7; j++) {
        const time = timetable[j] ? timetable[j] : [];
        document.getElementById(`day_box_${i}_${j}`).innerHTML = "";
        this.state.applicatios[i][j] = [];
        for (let k = 0; k < time.length; k++) {
          let pare = time[k];
          document.getElementById(`day_box_${i}_${j}`).innerHTML += 
          `<div id="application_${i}_${j}_${pare.application_id}" class="application-box raw-object w100-obj nullable-object raw-box ${await getStatusStyle(pare.status)} margin-v-normal padding-h-normal padding-v-normal">
            <div id="application_name_${i}_${j}_${pare.application_id}" class="application-box application-child raw-object w100-obj nullable-object text-big text-width-bold">${pare.name}</div>
            <div id="application_auditory_${i}_${j}_${pare.application_id}" class="application-box application-child raw-object w100-obj nullable-object text-normal margin-v-b-normal">${pare.buildings} к., ${pare.class_number} ауд.</div>
            <div id="application_status_${i}_${j}_${pare.application_id}" class="application-box application-child raw-object w100-obj nullable-object text-normal margin-v-b-normal">${await getStatusString(pare.status)}</div>
            <div id="application_description_${i}_${j}_${pare.application_id}" class="application-box application-child raw-object w100-obj nullable-object text-small">Описание: ${pare.description}</div>
          </div>`;
          this.state.applicatios[i][j].push(pare);
        }
      }
    }
    this.setState({ scheduleSet: true });
  }

  handleClick = async (event) => {
    const target = event.target;
    const targetId = target.id;
    const targetClasses = target.classList;
    

    if (targetClasses.contains('classroom-button')) {
      if (targetClasses.contains('button-good')) {
        if (this.state.choosenRooms.length > 1) {
          for (let i = 0; i < this.state.choosenRooms.length; i++) {
            let room = this.state.choosenRooms[i];
            if (room.id === targetId) {
              this.state.choosenRooms.splice(i, 1);
              target.classList.remove('button-good');
              target.classList.add('button-inactive');
              await this.generateSchedule();
              return;
            }
          }
        }
        else {
          alert("Должна быть выбрана как минимум 1 аудитория!");
        }
      }
      else {
        for (let i = 0; i < this.state.classRooms.length; i++) {
          let room = this.state.classRooms[i];
          if (room.id === targetId) {
            this.state.choosenRooms.push(room);
            target.classList.remove('button-inactive');
            target.classList.add('button-good');
            await this.generateSchedule();
            return;
          }
        }
      }
    }
    else if (targetClasses.contains('change-week')) {
      if (targetId == 'prev_week') {
        this.state.weekDistance -= 1;
      }
      else {
        this.state.weekDistance += 1;
      }
      await this.generateSchedule();
    }
    else if (targetClasses.contains('application-box')) {
      let idshnik = target.id.split("_");
      let i = idshnik[1];
      let j = idshnik[2];
      let pareId = idshnik[3];

      if (targetClasses.contains('application-child')) {
        i = idshnik[2];
        j = idshnik[3];
        pareId = idshnik[4];
      }

      console.log(idshnik);

      let pare = 0;
      console.log(this.state.applicatios[i][j]);
      
      for (let k = 0; k < this.state.applicatios[i][j].length; k++) {
        const application = this.state.applicatios[i][j][k];
        console.log(this.state.applicatios[i][j][k], pareId);
        if (application.application_id == pareId) {
          pare = application;
          console.log(this.state.applicatios[this.state.choosen_day[0]][this.state.choosen_day[1]][k]);
          break;
        }
      }

      console.log("mypare:", pare);

      document.getElementById('application_pare_name').innerText = pare.name;
      document.getElementById('application_pare_building').innerText = pare.buildings;
      document.getElementById('application_pare_auditory').innerText = pare.class_number;
      document.getElementById('application_pare_description').innerText = pare.description;
      document.getElementById('special_status').value = pare.status
      document.getElementById('save_status_changes').classList.remove('button-default');
      document.getElementById('save_status_changes').classList.add('button-inactive');
      document.getElementById('save_status_changes').id = `save_status_changes`;

      for (let ii = pare.status; ii <= 6; ii++) {
        document.getElementById(`status_${ii}`).removeAttribute('disabled');
      }

      this.state.choosenApplicationId = pareId;
      this.state.choosen_day = [i, j];

      document.getElementById('application_info').style.display = 'block';
    }
    else if (targetClasses.contains('black-content-window')) {
      document.getElementById('application_info').style.display = '';
      for (let i = 1; i <= 6; i++) {
        document.getElementById(`status_${i}`).setAttribute('disabled', 'disabled');
      }
      // document.getElementById(`${}_save_status_changes`).setAttribute('id', 'save_status_changes');
    }
    else if (targetClasses.contains('save_status_changes') && targetClasses.contains('button-default')) {
      let pareId = this.state.choosenApplicationId;
      let response = await changeApplicationStatus(pareId, document.getElementById('special_status').value, localStorage.getItem("keyGuardUserToken"))
      console.log(response);
      document.getElementById('save_status_changes').classList.remove('button-default');
      document.getElementById('save_status_changes').classList.add('button-inactive');
      
      if (response != null) {
        document.getElementById('application_info').style.display = '';
        let day_object = document.getElementById(`application_${this.state.choosen_day[0]}_${this.state.choosen_day[1]}_${this.state.choosenApplicationId}`);
        day_object.classList.remove('button-default');
        day_object.classList.remove('button-inactive');
        day_object.classList.remove('button-good');
        day_object.classList.remove('button-wrong');
        day_object.classList.remove('button-warning');
        day_object.classList.remove('button-gifted');
        day_object.classList.add(await getStatusStyle(document.getElementById('special_status').value));
        document.getElementById(`application_status_${this.state.choosen_day[0]}_${this.state.choosen_day[1]}_${this.state.choosenApplicationId}`).innerText = await getStatusString(document.getElementById('special_status').value);
        
        for (let index = 0; index < this.state.applicatios[this.state.choosen_day[0]][this.state.choosen_day[1]].length; index++) {
          let applic = this.state.applicatios[this.state.choosen_day[0]][this.state.choosen_day[1]][index];
          if (applic.application_id == this.state.choosenApplicationId) {
            applic.status = parseInt(document.getElementById('special_status').value);
            this.state.applicatios[this.state.choosen_day[0]][this.state.choosen_day[1]][index] = applic;
            break;
          }
        }
        
        this.state.choosenApplicationId = '';
        this.state.choosen_day = [0, 0];
        

        for (let i = 1; i <= 6; i++) {
          document.getElementById(`status_${i}`).setAttribute('disabled', 'disabled');
        }
      }
    }
  };

  handleBuildingChange = async (event) => {
    document.getElementById('classrooms').innerHTML = '';
    await this.generateClassrooms();
  }

  handleStatusChange = async (event) => {
    document.getElementById(`save_status_changes`).classList.remove('button-inactive');
    document.getElementById(`save_status_changes`).classList.add('button-default');
  }

  handleStatusesFilter = async (event) => {
    await this.generateSchedule();
  }

  addChooseRoom = async (event) => {
    let nId = document.getElementById('add_classroom').value;
    let room;
    for (let i = 0; i < this.state.classRooms.length; i++) {
      const room_n = this.state.classRooms[i];
      if (room_n.id == nId) {
        room = room_n;
        break;
      }
    }
    this.state.choosenRooms.push(room);
    document.getElementById(`chooseRoom_${nId}`).setAttribute('disabled','disabled');
    document.getElementById('classrooms').innerHTML += `<div class="classroom-button raw-object nullable-object button-good auditory-object padding-h-big" id="${nId}">${room.number}</div>`;
  }
  
  render() {
    return (
      <div class="container content-center">
        <div class="middle-wall-object">
          <div class="raw-object w100-obj maxable-object margin-v-b-big">
            <div class="raw-object w100-obj padding-v-normal vertical-top content-left">
              <div class="raw-object w20-obj nullable-object padding-h-normal vertical-top">
                <div class="raw-object nullable-object w100-obj text-normal text-light text-width-weak">Корпус</div>
                <select id="building_field" class="raw-object nullable-object w100-obj text-normal text-bold text-default padding-h-normal"></select>
              </div><div class="raw-object w50-obj padding-h-normal vertical-top">
                <div class="raw-object nullable-object w100-obj text-normal text-light text-width-weak">Аудитории</div>
                <div id="classrooms" class="raw-object nullable-object w100-obj text-normal text-bold text-default box-borders padding-h-normal padding-v-normal">
                  
                </div>
              </div><div class="raw-object w30-obj padding-h-normal vertical-top">
                <div class="raw-object nullable-object w100-obj text-normal text-light text-width-weak">Недели</div>
                <div id="prev_week" class="change-week raw-object nullable-object text-huge text-bold text-default margin-h-r-normal">
                  ◀️
                </div><div id="next_week" class="change-week raw-object nullable-object text-huge text-bold text-default">
                  ▶️
                </div>
              </div><div class="raw-object w100-obj padding-h-normal vertical-top">
                <div class="raw-object nullable-object w100-obj text-normal text-light text-width-weak">Статусы</div>
                <div class="raw-object nullable-object w100-obj text-normal text-default text-width-normal">
                  <div class="raw-object nullable-object no-wrap vertical-middle padding-h-normal w33-obj">
                    <input type='checkbox' name='status_1' id='status_1_check' class="raw-object nullable-object checkbox vertical-middle"/>
                    <div class="raw-object nullable-object margin-h-l-normal vertical-middle no-wrap">Необработано</div>
                  </div><div class="raw-object nullable-object no-wrap vertical-middle padding-h-normal w33-obj">
                    <input type='checkbox' name='status_2' id='status_2_check' class="raw-object nullable-object checkbox vertical-middle"/>
                    <div class="raw-object nullable-object margin-h-l-normal vertical-middle no-wrap">Подтверждено</div>
                  </div><div class="raw-object nullable-object no-wrap vertical-middle padding-h-normal w33-obj">
                    <input type='checkbox' name='status_3' id='status_3_check' class="raw-object nullable-object checkbox vertical-middle"/>
                    <div class="raw-object nullable-object margin-h-l-normal vertical-middle no-wrap">Ключ получен</div>
                  </div><div class="raw-object nullable-object no-wrap vertical-middle padding-h-normal w33-obj">
                    <input type='checkbox' name='status_4' id='status_4_check' class="raw-object nullable-object checkbox vertical-middle"/>
                    <div class="raw-object nullable-object margin-h-l-normal vertical-middle no-wrap">Ключ сдан</div>
                  </div><div class="raw-object nullable-object no-wrap vertical-middle padding-h-normal w33-obj">
                    <input type='checkbox' name='status_5' id='status_5_check' class="raw-object nullable-object checkbox vertical-middle"/>
                    <div class="raw-object nullable-object margin-h-l-normal vertical-middle no-wrap">Отключено</div>
                  </div><div class="raw-object nullable-object no-wrap vertical-middle padding-h-normal w33-obj">
                    <input type='checkbox' name='status_6' id='status_6_check' class="raw-object nullable-object checkbox vertical-middle"/>
                    <div class="raw-object nullable-object margin-h-l-normal vertical-middle no-wrap">Недействительно</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="raw-object w100-obj maxable-object no-wrap">
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
        <div id="application_info" class="middle-wall-object black-content-window">
          <div class="raw-object w100-obj raw-box content-left">
            <div class="raw-object w100-obj nullable-object raw-box margin-v-normal padding-h-normal padding-v-normal">
              <div id="application_pare_name" class="raw-object w100-obj nullable-object text-big text-width-bold">Пара</div>
              <div class="raw-object w100-obj nullable-object text-normal margin-v-b-normal">
                <div class="raw-object nullable-object text-normal text-width-bold margin-h-r-normal">
                  Корпус:
                </div><div id="application_pare_building" class="raw-object nullable-object text-normal text-width-normal">
                </div>
              </div>
              <div class="raw-object w100-obj nullable-object text-normal margin-v-b-normal">
                <div class="raw-object nullable-object text-normal text-width-bold margin-h-r-normal">
                  Аудитория:
                </div><div id="application_pare_auditory" class="raw-object nullable-object text-normal text-width-normal">
                </div>
              </div>
              <div class="raw-object w100-obj nullable-object text-normal margin-v-b-normal">
                <div class="raw-object nullable-object text-normal text-width-bold margin-h-r-normal">
                  Статус:
                </div><select id="special_status" class="raw-object w30-obj nullable-object text-normal text-width-bold">
                  <option id="status_1" value="1" disabled>Не обработано</option>
                  <option id="status_2" value="2" disabled>Подтверждено</option>
                  <option id="status_3" value="3" disabled>Ключ получен</option>
                  <option id="status_4" value="4" disabled>Ключ сдан</option>
                  <option id="status_5" value="5" disabled>Отклонено</option>
                  <option id="status_6" value="6" disabled>Недействительно</option>
                </select>
              </div>
              <div class="raw-object w100-obj nullable-object text-normal">
                <div class="raw-object nullable-object text-normal text-width-bold margin-h-r-normal vertical-top">
                  Описание:
                </div><div id="application_pare_description" class="raw-object nullable-object text-normal text-width-normal">
                </div>
              </div>
              <div id='save_status_changes' class="save_status_changes 100w-obj button-inactive text-width-bold text-normal margin-v-normal content-center padding-v-normal raw-box">Сохранить</div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default TimetablePage;