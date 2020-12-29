import { HasName } from "./HasName";
export interface ResourceMixin extends HasName {
  title?: string;
  description?: string;
  author?: string;
}
