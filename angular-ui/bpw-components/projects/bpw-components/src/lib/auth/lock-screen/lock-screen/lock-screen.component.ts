import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FuseConfigService } from '../../../common/services/config.service';
import { fuseAnimations } from '../../../common/animations';
import { authLayoutConfig } from '../../config/auth.config';

@Component({
    selector     : 'lock-screen',
    templateUrl  : './lock-screen.component.html',
    styleUrls    : ['./lock-screen.component.scss'],
    encapsulation: ViewEncapsulation.None,
    animations   : fuseAnimations
})
export class LockScreenComponent implements OnInit
{
    lockForm: FormGroup;

    /**
     * Constructor
     *
     * @ param {FuseConfigService} _fuseConfigService
     * @ param {FormBuilder} _formBuilder
     */
    constructor(
        private _fuseConfigService: FuseConfigService,
        private _formBuilder: FormBuilder
    ) {
        // Configure the layout
        this._fuseConfigService.config = {
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
                    value   : 'Katherine',
                    disabled: true
                }, Validators.required
            ],
            password: ['', Validators.required]
        });
    }
}
