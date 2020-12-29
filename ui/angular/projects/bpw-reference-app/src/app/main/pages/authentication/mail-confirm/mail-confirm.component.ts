import { Component, ViewEncapsulation } from '@angular/core';

import { UIConfigService, wcmAnimations } from 'bpw-common';

@Component({
    selector     : 'mail-confirm',
    templateUrl  : './mail-confirm.component.html',
    styleUrls    : ['./mail-confirm.component.scss'],
    encapsulation: ViewEncapsulation.None,
    animations   : wcmAnimations
})
export class MailConfirmComponent
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
