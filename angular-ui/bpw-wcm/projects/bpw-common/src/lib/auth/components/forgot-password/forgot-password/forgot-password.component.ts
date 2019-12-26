import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { UIConfigService } from '../../../../common/services/config.service';
import { wcmAnimations } from '../../../../common/animations/wcm-animations';
import { authLayoutConfig } from '../../../config/auth.config';

@Component({
    selector     : 'forgot-password',
    templateUrl  : './forgot-password.component.html',
    styleUrls    : ['./forgot-password.component.scss'],
    encapsulation: ViewEncapsulation.None,
    animations   : wcmAnimations
})
export class ForgotPasswordComponent implements OnInit {
    forgotPasswordForm: FormGroup;

    /**
     * Constructor
     *
     * @ param {UIConfigService} _uiConfigService
     * @ param {FormBuilder} _formBuilder
     */
    constructor(
        private _uiConfigService: UIConfigService,
        private _formBuilder: FormBuilder
    ) {
            // Configure the layout
            this._uiConfigService.config = {
                ...authLayoutConfig
            };
    }

    // -----------------------------------------------------------------------------------------------------
    // @ Lifecycle hooks
    // -----------------------------------------------------------------------------------------------------

    /**
     * On init
     */
    ngOnInit(): void {
        this.forgotPasswordForm = this._formBuilder.group({
            email: ['', [Validators.required, Validators.email]]
        });
    }
}
