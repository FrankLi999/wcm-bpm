export interface WcmItemFilter {
  wcmPath: string;
  nodeTypes?: string[];
  conditions?: {
    [nodeType: string]: {
      [property: string]: string;
    };
  };
  filter?: string;
  sortDirection?: string;
  pageIndex?: number;
  pageSize?: number;
}
