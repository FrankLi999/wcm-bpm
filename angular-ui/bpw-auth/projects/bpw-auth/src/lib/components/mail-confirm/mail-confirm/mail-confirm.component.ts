import { Component, ViewEncapsulation } from '@angular/core';

import { FuseConfigService } from 'bpw-components';
import { fuseAnimations } from 'bpw-components';
import { authLayoutConfig } from '../../../config/auth.config';
@Component({
    selector     : 'mail-confirm',
    templateUrl  : './mail-confirm.component.html',
    styleUrls    : ['./mail-confirm.component.scss'],
    encapsulation: ViewEncapsulation.None,
    animations   : fuseAnimations
})
export class MailConfirmComponent
{
    /**
     * Constructor
     *
     * @ param {FuseConfigService} _fuseConfigService
     */
    constructor(
        private _fuseConfigService: FuseConfigService
    ) {
        // Configure the layout
        this._fuseConfigService.config = {
            ...authLayoutConfig
        };
    }
}
