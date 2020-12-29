import { Component, ViewEncapsulation } from '@angular/core';

import { wcmAnimations } from 'bpw-common';

@Component({
    selector     : 'chat-start',
    templateUrl  : './chat-start.component.html',
    styleUrls    : ['./chat-start.component.scss'],
    encapsulation: ViewEncapsulation.None,
    animations   : wcmAnimations
})
export class ChatStartComponent
{
    constructor()
    {
    }
}
