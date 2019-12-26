import { Component } from '@angular/core';

import { SidebarService } from 'bpw-common';

@Component({
    selector   : 'carded-left-sidebar-1',
    templateUrl: './left-sidebar-1.component.html',
    styleUrls  : ['./left-sidebar-1.component.scss']
})
export class CardedLeftSidebar1Component
{
    /**
     * Constructor
     *
     * @param {SidebarService} _sidebarService
     */
    constructor(
        private _sidebarService: SidebarService
    )
    {
    }

    // -----------------------------------------------------------------------------------------------------
    // @ Public methods
    // -----------------------------------------------------------------------------------------------------

    /**
     * Toggle sidebar
     *
     * @param name
     */
    toggleSidebar(name): void
    {
        this._sidebarService.getSidebar(name).toggleOpen();
    }
}
