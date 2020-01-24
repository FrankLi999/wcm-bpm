import { ResourceNode } from './ResourceNode';
export interface Category {
    name: string;
    categories?: Category[];
}