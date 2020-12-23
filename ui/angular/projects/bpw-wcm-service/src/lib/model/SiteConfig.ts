import { UILayout } from "bpw-common";
import { WcmAuthority } from "./WcmAuthority";
import { ThemeColors } from "./ThemeColors";
export interface SiteConfig {
  repository: string;
  workspace: string;
  library: string;
  rootSiteArea: string;
  name: string;
  themeColors: ThemeColors;
  customScrollbars: boolean;
  direction?: string;
  layout: UILayout;
  wcmAuthority?: WcmAuthority;
}
