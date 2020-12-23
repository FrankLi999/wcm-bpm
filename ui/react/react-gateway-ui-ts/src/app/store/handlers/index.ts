import { combineReducers } from '@reduxjs/toolkit';
import dialog from './handlerDialogSlice';

const handlerReducers = combineReducers({
  dialog,
});

export default handlerReducers;
