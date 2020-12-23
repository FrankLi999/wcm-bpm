import { UIConfig } from "bpw-common";
/**
 * Default WCM Configuration
 *
 * You can edit these options to change the default options. All these options also can be
 * changed per component basis. See `app/authentication/login/login.component.ts`
 * constructor method to learn more about changing these options per component basis.
 */

export const authLayoutConfig: UIConfig = {
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
