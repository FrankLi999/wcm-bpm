import { BaseFormGroup } from "./BaseFormGroup";
import { FormColumn } from "./FormColumn";
export interface FormRow extends BaseFormGroup {
  rowName?: string;
  rowTitle?: string;
  order?: number;
  columns: FormColumn[];
}
