import { Component } from '@angular/core';

import { SidebarService } from 'bpw-common';

@Component({
    selector   : 'carded-right-sidebar-tabbed-1',
    templateUrl: './right-sidebar-tabbed-1.component.html',
    styleUrls  : ['./right-sidebar-tabbed-1.component.scss']
})
export class CardedRightSidebarTabbed1Component
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
