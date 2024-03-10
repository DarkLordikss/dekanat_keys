import {Form} from "react-bootstrap";
import React from "react";
import RoleSelector from "./RoleSelector";

export default function UserItem(props) {
    console.log(props.index);
    return (
        <tr>
            <td>{props.index}</td>
            <td>{props.name}</td>
            <td>{props.mail}</td>
            <td>
                <RoleSelector role={props.role} id={props.id}/>
            </td>

        </tr>
    )
}