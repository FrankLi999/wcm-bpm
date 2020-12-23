import { BaseContentItemElement } from "./BaseContentItemElement";
export interface BinraryElement extends BaseContentItemElement {
  value: ArrayBuffer;
  encoding: string;
}
