import { BaseFormGroup } from "./BaseFormGroup";
import { VisbleCondition } from "./VisbleCondition";
export interface FormColumn {
  id: string;
  fxFlex: number;
  order?: number;
  formControls?: string[];
  formGroups?: BaseFormGroup[];
  condition?: VisbleCondition;
}
