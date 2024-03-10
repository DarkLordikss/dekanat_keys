import React, { useState, useEffect } from 'react';
import { Form } from 'react-bootstrap';
import { updateRole } from './Connector';

const RoleSelector = (props) => {
    const [selectedValue, setSelectedValue] = useState('');

    useEffect(() => {
        if (props.role === 'Студент') {
            setSelectedValue('2');
        } else if (props.role === 'Преподаватель') {
            setSelectedValue('1');
        }
    }, [props.role]);

    const handleChange = async (event) => {
        let newValue = event.target.value;
        setSelectedValue(newValue);
        newValue === '2' ? newValue = '1' : newValue = '2';
        console.log(newValue);
        await updateRole(props.id, props.role, newValue);
    }

    return (
        <Form.Select key={selectedValue} aria-label="Default select example" value={selectedValue} onChange={handleChange}>
            <option value="2">Студент</option>
            <option value="1">Преподаватель</option>
        </Form.Select>
    );
}

export default RoleSelector;

