import { Component } from '@angular/core';

import { SidebarService } from 'bpw-common';

@Component({
    selector   : 'carded-right-sidebar-tabbed-2',
    templateUrl: './right-sidebar-tabbed-2.component.html',
    styleUrls  : ['./right-sidebar-tabbed-2.component.scss']
})
export class CardedRightSidebarTabbed2Component
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
