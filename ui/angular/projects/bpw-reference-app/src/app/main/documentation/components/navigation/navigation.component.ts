import { Component } from '@angular/core';

import { NavigationService } from 'bpw-common';
@Component({
    selector   : 'docs-components-navigation',
    templateUrl: './navigation.component.html',
    styleUrls  : ['./navigation.component.scss']
})
export class DocsComponentsNavigationComponent
{
    navigation: any;
    hidden: boolean;

    /**
     * Constructor
     */
    constructor(
        private _navigationService: NavigationService
    )
    {
        // Set the defaults
        this.hidden = false;
    }

    // -----------------------------------------------------------------------------------------------------
    // @ Public methods
    // -----------------------------------------------------------------------------------------------------

    /**
     * Show/hide calendar menu item
     */
    showHideCalendarMenuItem(): void
    {
        // Toggle the visibility
        this.hidden = !this.hidden;

        // Update the calendar menu item
        this._navigationService.updateNavigationItem('calendar', {
            hidden: this.hidden
        });
    }

    /**
     * Update mail badge
     */
    updateMailBadge(): void
    {
        // Update the badge title
        this._navigationService.updateNavigationItem('mail', {
                badge: {
                    title: 35
                }
        });
    }

    /**
     * Add subitem to the calendar
     */
    addSubitemToCalendar(): void
    {
        // Prepare the new nav item
        const newNavItem = {
            id   : 'sub-item',
            title: 'Sub Item',
            type : 'item',
            url  : '/apps/calendar'
        };

        this._navigationService.updateNavigationItem('calendar', {
            type: 'collapsable',
            children: [
                newNavItem
            ]
        });
    }

    /**
     * Add a nav item with custom function
     */
    addNavItemWithCustomFunction(): void
    {
        // Prepare the new nav item
        const newNavItem = {
            id      : 'custom-item',
            title   : 'Custom Item',
            type    : 'item',
            function: () => {
                alert('Custom function!');
            }
        };

        // Add the new nav item at the beginning of the navigation
        this._navigationService.addNavigationItem(newNavItem, 'start');
    }

    /**
     * Remove the dashboard menu item
     */
    removeDashboards(): void
    {
        this._navigationService.removeNavigationItem('dashboards');
    }

    /**
     * Register a new navigation and toggle to it
     */
    registerNewNavigationAndToggle(): void
    {
        const adminNav = [
            {
                id      : 'admin',
                title   : 'Admin',
                type    : 'group',
                icon    : 'apps',
                children: [
                    {
                        id   : 'users',
                        title: 'Users',
                        type : 'item',
                        icon : 'person',
                        url  : '/apps/dashboards/analytics'
                    },
                    {
                        id   : 'payments',
                        title: 'Payments',
                        type : 'item',
                        icon : 'attach_money',
                        url  : '/apps/academy'
                    }
                ]
            },
            {
                id      : 'control-panel',
                title   : 'Control Panel',
                type    : 'group',
                icon    : 'apps',
                children: [
                    {
                        id   : 'cron-jobs',
                        title: 'Cron Jobs',
                        type : 'item',
                        icon : 'settings',
                        url  : '/apps/file-manager'
                    },
                    {
                        id   : 'maintenance-mode',
                        title: 'Maintenance Mode',
                        type : 'item',
                        icon : 'build',
                        url  : '/apps/todo'
                    }
                ]
            }
        ];

        // Register the new navigation
        this._navigationService.register('admin-nav', adminNav);

        // Set the current navigation
        this._navigationService.setCurrentNavigation('admin-nav');
    }
}
