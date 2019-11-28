import { Component, ViewEncapsulation } from '@angular/core';

import { fuseAnimations } from 'bpw-components';

@Component({
    selector     : 'profile',
    templateUrl  : './profile.component.html',
    styleUrls    : ['./profile.component.scss'],
    encapsulation: ViewEncapsulation.None,
    animations   : fuseAnimations
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
