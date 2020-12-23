import { BaseFormGroup } from "./BaseFormGroup";
import { VisbleCondition } from "./VisbleCondition";
export interface FormStep {
  stepName: string;
  stepTitle?: string;
  order?: number;
  formGroups: BaseFormGroup[];
  consition?: VisbleCondition;
}
