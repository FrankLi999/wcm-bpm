import { Component, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';
import { Subject } from 'rxjs';
import { filter, takeUntil } from 'rxjs/operators';
import { select, Store } from '@ngrx/store';

import { Navigation } from '../../../../../common/types/navigation'; 
import { UIConfigService } from '../../../../../common/services/config.service'; 
import { NavigationService } from '../../../../../common/components/navigation/navigation.service';
import { SidebarService } from '../../../../../common/components/sidebar/sidebar.service';

import { AppConfigurationState } from '../../../../store/reducers/navigation.reducers';
import { getNavigation } from '../../../../store/selectors/navigation.selectors';

@Component({
    selector     : 'navbar-horizontal-style-1',
    templateUrl  : './style-1.component.html',
    styleUrls    : ['./style-1.component.scss'],
    encapsulation: ViewEncapsulation.None
})
export class NavbarHorizontalStyle1Component implements OnInit, OnDestroy
{
    uiConfig: any;
    navigation: Navigation[];

    // Private
    private _unsubscribeAll: Subject<any>;

    /**
     * Constructor
     *
     * @ param {UIConfigService} _uiConfigService
     * @ param {NavigationService} _navigationService
     * @ param {SidebarService} _sidebarService
     */
    constructor(
        private _uiConfigService: UIConfigService,
        private _navigationService: NavigationService,
        private _sidebarService: SidebarService,
        private _store: Store<AppConfigurationState>
    )
    {
        // Set the private defaults
        this._unsubscribeAll = new Subject();
    }

    // -----------------------------------------------------------------------------------------------------
    // @ Lifecycle hooks
    // -----------------------------------------------------------------------------------------------------

    /**
     * On init
     */
    ngOnInit(): void {
        this._store.pipe(select(getNavigation), takeUntil(this._unsubscribeAll)).subscribe(
          (navigation: Navigation[]) => {
            if (navigation) {
                this.navigation = navigation
            };
        });

        // Get current navigation
        this._navigationService.onNavigationChanged
            .pipe(
                filter(value => value !== null),
                takeUntil(this._unsubscribeAll)
            )
            .subscribe(() => {
                this.navigation = this._navigationService.getCurrentNavigation();
            });

        // Subscribe to the config changes
        this._uiConfigService.config
            .pipe(takeUntil(this._unsubscribeAll))
            .subscribe((config) => {
                this.uiConfig = config;
            });
    }

    /**
     * On destroy
     */
    ngOnDestroy(): void
    {
        // Unsubscribe from all subscriptions
        this._unsubscribeAll.next();
        this._unsubscribeAll.complete();
    }
}
