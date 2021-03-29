// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  hmr: false,
  wcmAuthoringBaseUrl: "mysite",
  wcmAuthoringDefaultUrl: "/mysite/home",
  wcmRepository: "bpwizard",
  wcmWorkspace: "default",
  wcmLibrary: "mysite",
  wcmSiteConfig: "mysite",
  oauth2RedirectUrl: "http://mysite:6009/oauth2/redirect",
  // wcmApiBaseUrl: "",
  // bpmApiBaseUrl: ""
  wcmApiBaseUrl: "http://wcm-site.bpwizard.com:28082",
  bpmApiBaseUrl: "http://bpm-server.bpwizard.com:28081"
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
