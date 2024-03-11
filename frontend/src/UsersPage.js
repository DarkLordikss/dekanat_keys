import React, {useState} from 'react';
import UserItem from "./UserItem";
import {
    checkAuth,
    getUsers
} from "./Connector.js";
import {Button, Table, Form, Alert} from "react-bootstrap";

class UsersPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            errors: null,
            isLoaded: false,
            users: [],
            filterRole: {
                student: true,
                teacher: true
            }
        };
    }

    handleFilterChange = (role) => {
        this.setState((prevState) => ({
            filterRole: {
                ...prevState.filterRole,
                [role]: !prevState.filterRole[role]
            }
        }));
    }


    async componentDidMount() {
        await this.UsersList();

        let authorized = checkAuth();
        if (!authorized) {
            window.location.href = '/';
        }

        console.log(this.state.users);
    }

    function
    async UsersList(props) {
        let response = await getUsers(1, localStorage.getItem("keyGuardUserToken"));
        console.log(response);
        this.setState({
            isLoaded: true,
            users: response
        }, () => {
            console.log(this.state);
        });

    }


    render() {
        const {error, isLoaded, users, filterRole} = this.state;
        console.log(filterRole.student)
        if (!isLoaded) {
            return <div>Loading...</div>;
        } else {
            const filteredUsers = users.filter(user => {
                return (filterRole.student && user.role === 'Студент') || (filterRole.teacher && user.role === 'Преподаватель');
            });

            return (
                <div>
                    <div className="middle-wall-object raw-box">
                        <div className="bigger-object margin-h-huge margin-v-huge">
                            <Alert key='light' variant='light'>
                                <Form>
                                    <div key="checkbox" className="">
                                        <Form.Check
                                            inline
                                            label="Студент"
                                            name="group1"
                                            type="checkbox"
                                            id="checkbox-1"
                                            checked={filterRole.student}
                                            onChange={() => this.handleFilterChange('student')}
                                        />
                                        <Form.Check
                                            inline
                                            label="Преподаватель"
                                            name="group1"
                                            type="checkbox"
                                            id="checkbox-2"
                                            checked={filterRole.teacher}
                                            onChange={() => this.handleFilterChange('teacher')}
                                        />
                                    </div>
                                </Form>
                            </Alert>
                            <Alert key='light' variant='light'>
                                <Table striped bordered hover size="sm" className="align-middle">
                                    <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Имя Пользователя</th>
                                        <th>Почта</th>
                                        <th>Роль</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {
                                        filteredUsers.map((value, index) => (
                                            <UserItem key={index} index={index + 1} name={value.full_name}
                                                      mail={value.email} role={value.role} id={value.id}/>
                                        ))
                                    }
                                    </tbody>
                                </Table>
                            </Alert>
                        </div>
                    </div>
                </div>
            );
        }
    }
}
    export default UsersPage;