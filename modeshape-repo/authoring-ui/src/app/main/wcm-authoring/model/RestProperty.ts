import { RestItem } from './RestItem'; 
export interface RestProperty extends RestItem {
    values: string[];
    multiValued: boolean;
}