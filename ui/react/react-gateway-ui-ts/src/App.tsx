import React from 'react';
import 'react-perfect-scrollbar/dist/css/styles.css';
import { Provider } from 'react-redux';
import { Router } from 'react-router-dom';
import { AuthState } from 'bpw-auth/elements/AuthState';
import store from './app/store';
import history from 'bpw-common/history';
import AuthorizationGuard from 'bpw-common/elements/AuthorizationGuard';
import BpwLayout from 'bpw-common/elements/BpwLayout';
import BpwTheme from 'bpw-common/elements/BpwTheme';
import AppContext from 'bpw-common/AppContext';
import BaseStyles from 'bpw-common/elements/BaseStyles';
import CustomeStyles from 'bpw-common/elements/CustomeStyles';

import routes from './app/config/routesConfig';
import wcmElements from './app/config/wcmElements';
import dynamicElements from './app/config/dynamicElements';
import { resetWcmSystem } from 'bpw-wcm/store/wcmSystemSlice';
import { clearEnum } from './app/store/system/enumSlice';

const customeCleanup = (dispatch) => {
  dispatch(clearEnum());
  dispatch(resetWcmSystem());
}

function App() {
  return (
    <AppContext.Provider
      value={{
        routes,
        wcmElements,
        dynamicElements,
        customeCleanup
      }}
    >
      <Provider store={store}>
        <Router history={history}>
          <AuthState>
            <AuthorizationGuard>
              <BpwTheme>
                <BaseStyles />
                <CustomeStyles />
                <BpwLayout />
              </BpwTheme>
            </AuthorizationGuard>
          </AuthState>
        </Router>
      </Provider>
    </AppContext.Provider>
  );
}

export default App;
