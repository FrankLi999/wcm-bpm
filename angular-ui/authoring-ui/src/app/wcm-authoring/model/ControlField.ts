import { HasName } from './HasName';
import { ControlFieldMetadata } from './ControlFieldMetadata'
export interface ControlField extends HasName {
  title: string;
  icon: string;
  controlFieldMetadata:  ControlFieldMetadata[];
}