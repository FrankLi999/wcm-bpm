import { WcmAuthority } from "./WcmAuthority";
export interface DraftItem {
  name: string;
  id: string;
  title: string;
  description: string;
  repository: string;
  wcmPath: string;
  processInstanceId: string;
  reviewer: string;
  editor: string;
  author: string;
  wcmAuthority: WcmAuthority;
}
