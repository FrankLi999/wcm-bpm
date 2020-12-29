import { FormControl } from "./FormControl";
import { JavascriptFunction } from "./JavascriptFunction";

export interface Constraint {
  enumeration?: string[];
  defaultValues?: string[];
  constant: string;
  contentMediaType: string;
  contentEncoding: string;
}

export interface ObjectConstraint {
  minProperties?: number;
  maxProperties?: number;
  propertyNamePattern?: string;
  allowAdditionalProperties?: boolean;
  additionalProperties?: { [key: string]: FormControl };
  patternProperties?: { [key: string]: FormControl };
  schemaDependencies?: { [key: string]: FormControl };
  dependencies?: { [key: string]: string[] };
}

export interface StringConstraint {
  minLength?: number;
  maxLength?: number;
  pattern?: string;
  format?: string;
}

export interface NumberConstraint {
  multipleOf?: number;
  exclusiveMaximum?: boolean;
  maximum?: number;
  exclusiveMinimum?: boolean;
  minimum?: number;
}

export interface ArrayConstraint {
  minItems?: number;
  maxItems?: number;
  uniqueItems?: boolean;
  items?: string; //Items type
  contains?: string; //contains type
  additionalItems?: { [key: string]: FormControl };
}

export interface CustomConstraint {
  javascriptFunction: JavascriptFunction[];
}
