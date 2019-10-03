export interface SiteNavigatorFilter {
  nodePath: string;
  nodeTypes?: string[];
  filters?: {
    [nodeType: string] : {
        [property: string]: string[]
    }
  }    
}