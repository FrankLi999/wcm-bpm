import { Component } from '@angular/core';

import { SidebarService } from 'bpw-common';

@Component({
    selector   : 'simple-right-sidebar-4',
    templateUrl: './right-sidebar-3.component.html',
    styleUrls  : ['./right-sidebar-3.component.scss']
})
export class SimpleRightSidebar3Component
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
