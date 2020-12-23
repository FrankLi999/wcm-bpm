import { combineReducers } from '@reduxjs/toolkit';

const createReducer = (reducers) =>
  combineReducers({
    ...reducers,
  });

export default createReducer;
