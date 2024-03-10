import React from 'react';
import UserItem from "./UserItem";
import RoleSelector from "./RoleSelector";
import {
    checkAuth,
    getUsers
} from "./Connector.js";
import {Button, Table, Form, Alert} from "react-bootstrap";

class UsersPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            errors : null,
            isLoaded : false,
            users : []
        };
    }

    async componentDidMount() {
        await this.UsersList();

        let authorized = checkAuth();
        if (!authorized) {
            window.location.href = '/';
        }

        console.log(this.state.users);


        /*for (let i = 0; i < users.length; i++) {
            let user = users[i];
            this.state.users.push(user);
        }*/
    }



    function
    async UsersList(props) {
        let response = await getUsers(1, localStorage.getItem("keyGuardUserToken"));
        console.log(response);
        this.setState({
            isLoaded : true,
            users : response
        }, () => {
            console.log(this.state);
        });

    }


    render() {
        const {error, isLoaded, users} = this.state;
        return (<div>
            <div className="middle-wall-object raw-box">
                <div className="bigger-object margin-h-huge margin-v-huge">
                    <Alert key='light' variant='light'>
                        <Form >
                            <div key="checkbox" className="">
                                <Form.Check
                                    inline
                                    label="Студент"
                                    name="group1"
                                    type="checkbox"
                                    id={`checkbox-1`}
                                />
                                <Form.Check
                                    inline
                                    label="Преподаватель"
                                    name="group1"
                                    type="checkbox"
                                    id={`checkbox-2`}
                                />
                            </div>
                        </Form>
                    </Alert>
                    <Alert key='light' variant='light'>
                        <Table striped bordered hover size="sm" className="align-middle">
                            <thead>
                            {/*<Button  variant="outline-success">Подтвердить</Button>{' '}*/}
                            <tr>
                                <th>#</th>
                                <th>Имя Пользователя</th>
                                <th>Почта</th>
                                <th>Роль</th>
                            </tr>
                            </thead>
                            <tbody>
                            {
                                users.map((value, index) => {
                                    return <UserItem index={index + 1} name = {value.full_name} mail = {value.email} role = {value.role} id = {value.id}
                                        />})
                            }
                            </tbody>
                        </Table>
                    </Alert>
                </div>
            </div>
        </div>


        )
    }
}
export default UsersPage;