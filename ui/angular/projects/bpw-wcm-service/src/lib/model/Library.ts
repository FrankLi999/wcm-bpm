import { WcmAuthority } from "./WcmAuthority";
export interface Library {
  repository: string;
  workspace: string;
  name: string;
  title?: string;
  language?: string;
  description?: string;
  wcmAuthority?: WcmAuthority;
}
