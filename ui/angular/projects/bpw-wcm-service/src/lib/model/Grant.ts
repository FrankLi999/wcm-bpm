import { WcmPermission } from "./WcmPermission";
export interface Grant {
  wcmPath?: string;
  permissions?: WcmPermission[];
}
