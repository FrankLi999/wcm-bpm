import { WcmAuthority } from "./WcmAuthority";
export interface Category {
  repository?: string;
  workspace?: string;
  library?: string;
  name: string;
  parent?: string;
  wcmAuthority?: WcmAuthority;
}
