import { of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { from } from 'rxjs';
// import axios from 'axios';
import Axios from 'axios-observable';
import jwtDecode from 'jwt-decode';
import logoutUser from '../../store/userSlice';
/* eslint-disable camelcase */

class JwtService {
  init() {
    this.setInterceptors();
    return this.checkAccessToken();
  }

  setInterceptors = () => {
    axios.interceptors.response.use(
      (response) => {
        return response;
      },
      (err) => {
        return new Promise((resolve, reject) => {
          if (err.response.status === 401 && err.config && !err.config.__isRetryRequest) {
            // if you ever get an unauthorized response, logout the user
            // this.emit('onAutoLogout', 'Invalid access_token');
            logoutUser();
            this.setSession(null);
          }
          throw err;
        });
      }
    );
  };

  checkAccessToken = () => {
    const access_token = this.getAccessToken();
    if (!access_token) {
      return { event: 'NoAccessToken' };
    }

    if (this.isAuthTokenValid(access_token)) {
      this.setSession(access_token);
      // this.emit('onAutoLogin', true);
      return { event: 'AutoLogin' };
    } else {
      this.setSession(null);
      return { event: 'AutoLogout', message: 'access_token expired' };
    }
  };

  createUser = (data) => {
        //apiConfigService.baseUrl['auth-service']
    return from(axios.post('/core/api/users', data)).pipe(
      map((response) => {
        if (response.data.user) {
          this.setSession(response.data.access_token);
          return response.data.user;
        } else {
          return response.data.error;
        }
      })
    );
  };

  signInWithEmailAndPassword = (email, password) => {
        //apiConfigService.baseUrl['auth-service']
    return from(axios
      .post('/core/api/login?repository=bpwizard&workspace=default&library=apigateway&config=apigateway', {
        email,
        password,
      }))
      .pipe(
        map((response) => {
          this.setSession(response.data.accessToken);
          return response.data;
        })
      );
  };

  signInWithToken = () => {
        //apiConfigService.baseUrl['auth-service']
    return from(axios
      .get('/core/api/user-profile?repository=bpwizard&workspace=default&library=apigateway&config=apigateway'))
      .pipe(
        map((response) => {
          if (response.data) {
            return response.data;
          } else {
            this.logout();
            return new Error('Failed to login with token.');
          }
        }),
        catchError((error) => {
          this.logout();
          return of(new Error('Failed to login with token.'));
        })
      );
  };

  updateUserData = (user) => {
    return from(axios.post('/api/auth/user/update', {
      user,
    }));
  };

  setSession = (access_token) => {
    if (access_token) {
      axios.defaults.headers.common.Authorization = `Bearer ${access_token}`;
    } else {
      delete axios.defaults.headers.common.Authorization;
    }
  };

  logout = () => {
    this.setSession(null);
  };

  isAuthTokenValid = (access_token) => {
    if (!access_token) {
      return false;
    }
    const decoded = jwtDecode(access_token);

    const currentTime = Date.now() / 1000;
    if (decoded.exp < currentTime) {
      console.warn('access token expired');
      return false;
    }
    return true;
  };

  getAccessToken = () => {
    return sessionStorage.getItem('bpw_accessToken');
  };
}

const instance = new JwtService();

export default instance;
