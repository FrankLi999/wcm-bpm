import React from 'react';
import ToDoList from './components/ToDoList';
import './App.scss';

const App = () => {
  return (
    <div>
      <h1 className="todoHeader">React with TS Demo App</h1>
      <ToDoList />
    </div>
  );
}

export default App;