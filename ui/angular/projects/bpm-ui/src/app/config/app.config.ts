import { ApiConfig } from "bpw-rest-client";
import { appConfig, layoutConfig, UIConfig } from "bpw-common";
import { environment } from "../../environments/environment";

/**
 * Default WCM Configuration
 *
 * You can edit these options to change the default options. All these options also can be
 * changed per component basis. See `app/authentication/login/login.component.ts`
 * constructor method to learn more about changing these options per component basis.
 */

appConfig.baseUrl = environment.wcmAuthoringBaseUrl;
appConfig.defaultUrl = environment.wcmAuthoringDefaultUrl;
appConfig.repository = environment.wcmRepository;
appConfig.workspace = environment.wcmWorkspace;
appConfig.library = environment.wcmLibrary;
appConfig.siteConfig = environment.wcmSiteConfig;
appConfig.wcmApiBaseUrl = environment.wcmApiBaseUrl;
appConfig.bpmApiBaseUrl = environment.bpmApiBaseUrl;
appConfig.oauth2RedirectUrl = environment.oauth2RedirectUrl;
appConfig.layoutConfig = layoutConfig;

export const APP_OAUTH2_REDIRECT_URI = appConfig.oauth2RedirectUrl;
export const WCM_APP_API_BASE_URL = appConfig.wcmApiBaseUrl;
export const BPM_APP_API_BASE_URL = appConfig.bpmApiBaseUrl;

export const appApiConfig: ApiConfig = {
  apiBaseUrls: {
    "wcm": WCM_APP_API_BASE_URL,
    "bpm": BPM_APP_API_BASE_URL,
    "auth-service": WCM_APP_API_BASE_URL
  },
  accessToken: "accessToken",
  oauth2RedirectUrl: APP_OAUTH2_REDIRECT_URI,
  googleAuthUrl: `${WCM_APP_API_BASE_URL}/oauth2/authorization/google?redirect_uri=${APP_OAUTH2_REDIRECT_URI}`,
  facebookAuthUrl: `${WCM_APP_API_BASE_URL}/oauth2/authorization/facebook?redirect_uri=${APP_OAUTH2_REDIRECT_URI}`,
  githubAuthUrl: `${WCM_APP_API_BASE_URL}/oauth2/authorization/github?redirect_uri=${APP_OAUTH2_REDIRECT_URI}`,
};

export const appLayoutConfig: UIConfig = appConfig.layoutConfig;

