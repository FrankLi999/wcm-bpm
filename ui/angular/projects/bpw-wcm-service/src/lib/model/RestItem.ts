import { HasName } from './HasName';
export interface RestItem extends HasName {
    url: string;
    parentUrl: string;
}