export interface WcmNode {
  repository: string;
  workspace: string;
  id?: string;
  wcmPath: string;
  name: string;
  title?: string;
  nodeType: string;
  lastModified?: string;
  owner?: string;
  status?: string;
}
