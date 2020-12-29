import { FormControlLayout } from "./FormControlLayout";
import {
  Constraint,
  ObjectConstraint,
  StringConstraint,
  NumberConstraint,
  ArrayConstraint,
  CustomConstraint,
} from "./Constraint";
export interface FormControl {
  name: string;
  controlType: string;
  dataType?: string; //default Text
  format?: string;
  jcrDataType?: string; //default Text
  multiple?: boolean;

  autoCreate?: boolean;
  mandatory?: boolean;
  userSearchable?: boolean;
  systemIndexed?: boolean;
  showInList?: boolean;

  formControlLayout?: FormControlLayout;

  constraint?: Constraint;
  objectConstraint?: ObjectConstraint;
  stringConstraint?: StringConstraint;
  numberConstraint?: NumberConstraint;
  arrayConstraint?: ArrayConstraint;
  customConstraint?: CustomConstraint;

  formControls?: { [key: string]: FormControl };
}
