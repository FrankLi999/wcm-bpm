import { Pipe, PipeTransform } from '@angular/core';
import { Utils } from '../utils/utils';

@Pipe({name: 'filter'})
export class FilterPipe implements PipeTransform {
    /**
     * Transform
     *
     * @ param {any[]} mainArr
     * @ param {string} searchText
     * @ param {string} property
     * @ returns {any}
     */
    transform(mainArr: any[], searchText: string, property: string): any {
        return Utils.filterArrayByString(mainArr, searchText);
    }
}
