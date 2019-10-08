export interface WcmItemFilter {
  nodePath: string;
  nodeTypes?: string[];
  filters?: {
    [nodeType: string] : {
        [property: string]: string
    }
  }    
}