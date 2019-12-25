import { Component, ViewEncapsulation } from '@angular/core';

import { wcmAnimations } from 'bpw-components';

@Component({
    selector     : 'profile',
    templateUrl  : './profile.component.html',
    styleUrls    : ['./profile.component.scss'],
    encapsulation: ViewEncapsulation.None,
    animations   : wcmAnimations
})
export class ProfileComponent
{
    /**
     * Constructor
     */
    constructor()
    {

    }
}
