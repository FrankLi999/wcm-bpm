import { UIConfig } from "./common/types/ui-config";
export var layoutConfig: UIConfig = {
  // Color themes can be defined in src/app/app.theme.scss
  colorTheme: "theme-default",
  customScrollbars: true,
  layout: {
    title: "horizontal-layout",
    mode: "fullwidth",
    navbar: {
      primaryBackground: "wcm-light-200",
      secondaryBackground: "wcm-light-300",
      folded: false,
      display: false,
      position: "top",
      variant: "vertical",
    },
    toolbar: {
      customBackgroundColor: false,
      background: "wcm-light-300",
      display: false,
      position: "below",
    },
    footer: {
      customBackgroundColor: true,
      background: "wcm-light-300",
      display: false,
      position: "above-static",
    },
    leftSidePanel: {
      display: false,
    },
    rightSidePanel: {
      display: false,
    },
  },
};
export var appConfig = {
  components:{},
  baseUrl: "wcm-authoring",
  defaultUrl: "/wcm-authoring/site-explorer",
  repository: "bpwizard",
  workspace: "default",
  library: "camunda",
  siteConfig: "bpm",
  wcmApiBaseUrl: "",
  bpmApiBaseUrl: "",
  oauth2RedirectUrl: "",
  routeSubscriptions: [],
  layoutConfig: layoutConfig,
};
