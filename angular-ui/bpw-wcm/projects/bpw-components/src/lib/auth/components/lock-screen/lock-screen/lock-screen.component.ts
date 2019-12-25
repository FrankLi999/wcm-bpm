import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UIConfigService } from '../../../../common/services/config.service';
import { wcmAnimations } from '../../../../common/animations/wcm-animations';
import { authLayoutConfig } from '../../../config/auth.config';

@Component({
    selector     : 'lock-screen',
    templateUrl  : './lock-screen.component.html',
    styleUrls    : ['./lock-screen.component.scss'],
    encapsulation: ViewEncapsulation.None,
    animations   : wcmAnimations
})
export class LockScreenComponent implements OnInit
{
    lockForm: FormGroup;

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
    ngOnInit(): void
    {
        this.lockForm = this._formBuilder.group({
            username: [
                {
                    value   : 'Frank',
                    disabled: true
                }, Validators.required
            ],
            password: ['', Validators.required]
        });
    }
}
