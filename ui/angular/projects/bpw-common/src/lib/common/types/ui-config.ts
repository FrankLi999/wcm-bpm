export interface SidePanel {
  display: boolean;
}

export interface UILayout {
  title: string;
  mode: "fullwidth" | "boxed" | "container";
  navbar: {
    primaryBackground: string;
    secondaryBackground: string;
    display: boolean;
    style?: "fixed" | "static";
    folded: boolean;
    position: "left" | "right" | "top";
    variant: string;
  };
  toolbar: {
    customBackgroundColor: boolean;
    background: string;
    display: boolean;
    style?: "fixed" | "static";
    position:
      | "above"
      | "above-static"
      | "above-fixed"
      | "below"
      | "below-static"
      | "below-fixed";
  };
  footer: {
    customBackgroundColor: boolean;
    background: string;
    display: boolean;
    style?: "fixed" | "static";
    position:
      | "above"
      | "above-static"
      | "above-fixed"
      | "below"
      | "below-static"
      | "below-fixed";
  };
  leftSidePanel: SidePanel;
  rightSidePanel: SidePanel;
}

export interface UIConfig {
  colorTheme: string;
  customScrollbars: boolean;
  layout: UILayout;
}
