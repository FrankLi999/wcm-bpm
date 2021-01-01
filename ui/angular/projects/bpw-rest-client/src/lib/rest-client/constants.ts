// export const API_BASE_URL = 'http://192.168.0.168:28080';
export const API_BASE_URL = "";
export const ACCESS_TOKEN = "accessToken";

export const OAUTH2_REDIRECT_URI = "http://wcm-authoring:3009/oauth2/redirect";

export const GOOGLE_AUTH_URL =
  API_BASE_URL +
  "/oauth2/authorization/google?redirect_uri=" +
  OAUTH2_REDIRECT_URI;
export const FACEBOOK_AUTH_URL =
  API_BASE_URL +
  "/oauth2/authorization/facebook?redirect_uri=" +
  OAUTH2_REDIRECT_URI;
export const GITHUB_AUTH_URL =
  API_BASE_URL +
  "/oauth2/authorization/github?redirect_uri=" +
  OAUTH2_REDIRECT_URI;
