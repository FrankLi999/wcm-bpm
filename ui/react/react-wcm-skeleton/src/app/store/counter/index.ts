import { combineReducers } from '@reduxjs/toolkit';
import counter from './counterSlice';
const counterReducers = combineReducers({
  counter
});

export default counterReducers;