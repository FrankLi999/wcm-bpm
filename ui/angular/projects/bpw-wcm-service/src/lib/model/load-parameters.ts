export interface LoadParameters {
  repository: string;
  workspace: string;
  library?: string;
  filter: string;
  sortDirection: "asc" | "desc" | "";
  pageIndex: number;
  pageSize: number;
}
