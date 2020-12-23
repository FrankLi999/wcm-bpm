import React from 'react';
import reduxStore from './reduxStore';
import createReducer from './rootReducer';
export const injectReducer = (key, reducer) => {
  if (reduxStore.asyncReducers[key]) {
    return false;
  }
  reduxStore.asyncReducers[key] = reducer;
  reduxStore.store.replaceReducer(createReducer({ ...reduxStore.initialReducers, ...reduxStore.asyncReducers }));
  return reduxStore.store;
};
const withReducer = (key, reducer) => (WrappedComponent) => {
  injectReducer(key, reducer);
  return (props) => <WrappedComponent {...props} />;
};
export default withReducer;
