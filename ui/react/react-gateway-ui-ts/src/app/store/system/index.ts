import { combineReducers } from '@reduxjs/toolkit';
import dialog from './systemDialogSlice';
import systemEnum from './enumSlice';
const systemReducers = combineReducers({
  dialog,
  systemEnum,
});

export default systemReducers;
