import { BaseFormGroup } from "./BaseFormGroup";
import { VisbleCondition } from "./VisbleCondition";
export interface FormTab {
  tabName: string;
  tabTitle?: string;
  order?: number;
  formGroups: BaseFormGroup[];
  consition?: VisbleCondition;
}
