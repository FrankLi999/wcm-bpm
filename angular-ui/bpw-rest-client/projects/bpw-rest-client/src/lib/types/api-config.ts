// export const API_BASE_URL = 'http://192.168.0.168:8080';
// export const API_BASE_URL = 'http://localhost:8080';
// export const ACCESS_TOKEN = 'accessToken';

// export const OAUTH2_REDIRECT_URI = 'http://localhost:3000/oauth2/redirect'
// export const GOOGLE_AUTH_URL = API_BASE_URL + '/oauth2/authorize/google?redirect_uri=' + OAUTH2_REDIRECT_URI;
// export const FACEBOOK_AUTH_URL = API_BASE_URL + '/oauth2/authorize/facebook?redirect_uri=' + OAUTH2_REDIRECT_URI;
// export const GITHUB_AUTH_URL = API_BASE_URL + '/oauth2/authorize/github?redirect_uri=' + OAUTH2_REDIRECT_URI;
export interface ApiConfig {
  apiBaseUrl: string;
  accessToken: string;
  oauth2RedirectUrl: string;
  googleAuthUrl: string;
  facebookAuthUrl: string;
  githubAuthUrl: string;  
}