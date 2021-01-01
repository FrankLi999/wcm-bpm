import { ApiConfig } from "bpw-rest-client";
/**
 * Default UI Configuration
 *
 * You can edit these options to change the default options. All these options also can be
 * changed per component basis. See `app/authentication/login/login.component.ts`
 * constructor method to learn more about changing these options per component basis.
 */
import { appConfig, UIConfig, layoutConfig } from "bpw-common";

appConfig.baseUrl = "wcm-authoring";
appConfig.defaultUrl = "/wcm-authoring/site-explorer";
appConfig.repository = "bpwizard",
appConfig.workspace = "default",
appConfig.library = "camunda";
appConfig.siteConfig = " bpm";
appConfig.wcmApiBaseUrl = "",
appConfig.bpmApiBaseUrl = "",
appConfig.oauth2RedirectUrl = "";
appConfig.routeSubscriptions = [],
appConfig.layoutConfig = layoutConfig;

// declare var appConfig: any;
export const APP_OAUTH2_REDIRECT_URI = appConfig.oauth2RedirectUrl;
export const APP_API_BASE_URL = appConfig.wcmApiBaseUrl;
export const BPM_APP_API_BASE_URL = appConfig.bpmApiBaseUrl;
export const appApiConfig: ApiConfig = {
  apiBaseUrls:  {
    "wcm": APP_API_BASE_URL,
    "bpmm": BPM_APP_API_BASE_URL
  },
  accessToken: "accessToken",
  oauth2RedirectUrl: APP_OAUTH2_REDIRECT_URI,
  googleAuthUrl: `${APP_API_BASE_URL}//oauth2/authorization/google?redirect_uri=${APP_OAUTH2_REDIRECT_URI}`,
  facebookAuthUrl: `${APP_API_BASE_URL}//oauth2/authorization/facebook?redirect_uri=${APP_OAUTH2_REDIRECT_URI}`,
  githubAuthUrl: `${APP_API_BASE_URL}//oauth2/authorization/github?redirect_uri=${APP_OAUTH2_REDIRECT_URI}`,
};

export const appLayoutConfig: UIConfig = appConfig.layoutConfig;
