import React from 'react';
import styles from './ToDoList.module.scss';
import Button from '../Button';
import TextInput from '../TextInput';
import List from '../List';

type ToDoItem = {
    key: string,
    text: string
}


class ToDoList extends React.Component {
    state = {
        toDoItems: [],
        inputText: '',
        submitDisabled: true
    }

    addToDo = (text: string) => {
        const key = Math.random().toString();
        this.setState({toDoItems: [{text, key}, ...this.state.toDoItems], submitDisabled: true});
    }

    delToDo = (id: string) => {
        this.setState({toDoItems: this.state.toDoItems.filter((todo: ToDoItem) => todo.key !== id)})
    }

    handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        let text = event.target.value.replace(/\s/g,'');
        this.setState({inputText: event.target.value})
        if (text) {
            this.setState({submitDisabled: false})
        } else {
            this.setState({submitDisabled: true})
        }
    }

    handleSubmit = (event: React.FormEvent<HTMLFormElement>) =>{
        event.preventDefault();
        this.addToDo(this.state.inputText);
        this.setState({inputText: ''});
    }

    render(){
        return(
            <div className={styles.container}>
                <form className={styles.form} onSubmit={this.handleSubmit}>
                    <TextInput value={this.state.inputText} onChange={this.handleChange} />
                    <Button disabled={this.state.submitDisabled} type="submit">Add</Button>
                </form>
                <List items={this.state.toDoItems} removeItem={this.delToDo} />
            </div>
        )
    }
}

export default ToDoList;