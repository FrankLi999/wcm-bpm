import { UIConfig } from "bpw-common";
import { ApiConfig } from "bpw-rest-client";
import { appConfig, layoutConfig } from "bpw-common";
/**
 * Default WCM Configuration
 *
 * You can edit these options to change the default options. All these options also can be
 * changed per component basis. See `app/authentication/login/login.component.ts`
 * constructor method to learn more about changing these options per component basis.
 */
appConfig.baseUrl = "wcm-authoring";
appConfig.defaultUrl = "/wcm-authoring/site-explorer";
appConfig.repository = "bpwizard",
appConfig.workspace = "default",
appConfig.library = "camunda";
appConfig.siteConfig = " bpm";
appConfig.wcmApiBaseUrl = "",
appConfig.bpmApiBaseUrl = "",
appConfig.oauth2RedirectUrl = "http://wcm-authoring:3009/oauth2/redirect";
appConfig.routeSubscriptions = [],
appConfig.layoutConfig = layoutConfig;

export const APP_OAUTH2_REDIRECT_URI = appConfig.oauth2RedirectUrl;
export const APP_API_BASE_URL = appConfig.wcmApiBaseUrl;
export const appApiConfig: ApiConfig = {
  apiBaseUrls: {"wcm": APP_API_BASE_URL},
  accessToken: "accessToken",
  oauth2RedirectUrl: APP_OAUTH2_REDIRECT_URI,
  googleAuthUrl: `${APP_API_BASE_URL}/oauth2/authorization/google?redirect_uri=${APP_OAUTH2_REDIRECT_URI}`,
  facebookAuthUrl: `${APP_API_BASE_URL}/oauth2/authorization/facebook?redirect_uri=${APP_OAUTH2_REDIRECT_URI}`,
  githubAuthUrl: `${APP_API_BASE_URL}/oauth2/authorization/github?redirect_uri=${APP_OAUTH2_REDIRECT_URI}`,
};

export const appLayoutConfig: UIConfig = appConfig.layoutConfig;
