import { HasName } from "./HasName";
import { WcmAuthority } from "./WcmAuthority";
export interface ValidationRule extends HasName {
  repository?: string;
  workspace?: string;
  library?: string;

  title?: string;
  description?: string;
  type?: string;
  rule?: string;
  wcmAuthority?: WcmAuthority;
}
