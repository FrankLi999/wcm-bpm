import { HasName } from './HasName';
export interface Theme extends HasName {
  title: string;
  library: string;
  repositoryName: string;
  workspace: string;
}