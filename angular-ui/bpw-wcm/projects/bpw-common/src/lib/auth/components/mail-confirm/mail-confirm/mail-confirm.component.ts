import { Component, ViewEncapsulation } from '@angular/core';

import { UIConfigService } from '../../../../common/services/config.service';
import { wcmAnimations } from '../../../../common/animations/wcm-animations';
import { authLayoutConfig } from '../../../config/auth.config';
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
     * @ param {UIConfigService} _wcmConfigService
     */
    constructor(
        private _wcmConfigService: UIConfigService
    ) {
        // Configure the layout
        this._wcmConfigService.config = {
            ...authLayoutConfig
        };
    }
}
