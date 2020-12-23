import { combineReducers } from '@reduxjs/toolkit';
import wcmSystem from './wcmSystemSlice';

const wcmReducers = combineReducers({
  wcmSystem,
});
export default wcmReducers;
