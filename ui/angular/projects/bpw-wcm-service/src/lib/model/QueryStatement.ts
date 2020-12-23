import { HasName } from "./HasName";
import { WcmAuthority } from "./WcmAuthority";
export interface QueryStatement extends HasName {
  repository?: string;
  workspace?: string;
  library?: string;
 
  title?: string;
  query?: string;
  columns?: string[];
  wcmAuthority?: WcmAuthority;
}
