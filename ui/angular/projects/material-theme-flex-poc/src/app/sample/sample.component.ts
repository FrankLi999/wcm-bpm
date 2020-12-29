import { Component } from '@angular/core';

import { TranslationLoaderService } from 'bpw-common';
import { locale as english } from './i18n/en';
import { locale as turkish } from './i18n/tr';

@Component({
    selector   : 'sample',
    templateUrl: './sample.component.html',
    styleUrls  : ['./sample.component.scss']
})
export class SampleComponent
{
    /**
     * Constructor
     *
     * @param {TranslationLoaderService} _translationLoaderService
     */
    constructor(
        private _translationLoaderService: TranslationLoaderService
    )
    {
        this._translationLoaderService.loadTranslations(english, turkish);
    }
}
