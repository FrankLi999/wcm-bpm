import { configureStore } from '@reduxjs/toolkit';
import { combineEpics, createEpicMiddleware } from 'redux-observable';
import reduxStore from 'bpw-common/store/reduxStore';
import createReducer from 'bpw-common/store/rootReducer';
import bpw from 'bpw-common/store';
import auth from 'bpw-auth/store';
import wcm from 'bpw-wcm/store';
import i18n from 'bpw-common/store/i18n/i18nSlice';
import handlers from './handlers';
import system from './system';
import counter from './counter';
import counterEpic from './counter/counterEpic';
const initialReducers = { bpw, auth, wcm, i18n, handlers, system, counter };

if (process.env.NODE_ENV === 'development' && module.hot) {
  module.hot.accept('bpw-common/store/rootReducer', () => {
    const newRootReducer = require('bpw-common/store/rootReducer').default;
    store.replaceReducer(newRootReducer.createReducer(initialReducers));
  });
}
const epicMiddleware = createEpicMiddleware();
export const rootEpic = combineEpics(
  counterEpic
);
const middlewares = [];
middlewares.push(epicMiddleware);
if (process.env.NODE_ENV === 'development') {
  const { logger } = require(`redux-logger`);
  middlewares.push(logger);
}

const store = configureStore({
  reducer: createReducer(initialReducers),
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: ['dialog/openDialog', 'dialog/closeDialog', 'message/showMessage', 'message/hideMessage'],
      },
    }).concat(middlewares),
  devTools: process.env.NODE_ENV === 'development',
});
epicMiddleware.run(rootEpic);

reduxStore.asyncReducers = {};
reduxStore.initialReducers = initialReducers;
reduxStore.store = store;
export default store;
