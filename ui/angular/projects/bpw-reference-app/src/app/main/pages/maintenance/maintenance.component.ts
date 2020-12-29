import { Component, ViewEncapsulation } from '@angular/core';

import { UIConfigService, wcmAnimations } from 'bpw-common';

@Component({
    selector     : 'maintenance',
    templateUrl  : './maintenance.component.html',
    styleUrls    : ['./maintenance.component.scss'],
    encapsulation: ViewEncapsulation.None,
    animations   : wcmAnimations
})
export class MaintenanceComponent
{
    /**
     * Constructor
     *
     * @param {UIConfigService} _uiConfigService
     */
    constructor(
        private _uiConfigService: UIConfigService
    )
    {
        // Configure the layout
        this._uiConfigService.config = {
            layout: {
                navbar   : {
                    hidden: true
                },
                toolbar  : {
                    hidden: true
                },
                footer   : {
                    hidden: true
                },
                sidepanel: {
                    hidden: true
                }
            }
        };
    }
}
