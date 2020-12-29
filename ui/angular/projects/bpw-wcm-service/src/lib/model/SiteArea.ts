import { HasName } from "./HasName";
import { KeyValues } from "./KeyValues";
import { SearchData } from "./SearchData";
import { SiteAreaLayout } from "./SiteAreaLayout";
// import { NavigationBadge } from "./NavigationBadge";
import { ResourceMixin } from "./ResourceMixin";
import { WcmAuthority } from "./WcmAuthority";
export interface SiteArea extends HasName {
  repository: string;
  workspace: string;
  wcmPath?: string;
  name: string;

  // title?: string;
  // description?: string;
  // url?: string;
  // sorderOrder?: number;
  // friendlyURL?: string;
  // showOnMenu?: boolean;
  // siteConfig?: string;
  // contentPath?: string;
  // allowedFileExtension?: string;
  // allowedArtifactTypes?: string;

  // contentAreaLayout?: string;
  // cacheTTL?: number;
  // securePage?: boolean;
  // navigationId?: string;
  // navigationType?: string;
  // function?: string;
  // translate?: string;
  // icon?: string;
  // classes?:string;
  // exactMatch?: boolean;
  // externalUrl?: boolean;
  // openInNewTab?: boolean;

  elements?: { [key: string]: any };
  // properties?: { [key: string]: string };
  properties?: ResourceMixin;
  metadata?: KeyValues;
  searchData?: SearchData;
  siteAreaLayout?: SiteAreaLayout;
  // badge?: NavigationBadge;
  wcmAuthority?: WcmAuthority;
}
