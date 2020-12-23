import { ColumnValue } from "./ColumnValue";
export interface QueryResult {
  rows: { [key: string]: ColumnValue }[];
}
