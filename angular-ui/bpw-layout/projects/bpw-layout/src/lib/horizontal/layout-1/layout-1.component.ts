import { Component, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { select, Store } from '@ngrx/store';
import {
  FuseConfigService,
  FuseNavigation
} from 'bpw-components';
import { AppConfigurationState } from '../../store/reducers/navigation.reducers';
import { getNavigation } from '../../store/selectors/navigation.selectors';

@Component({
    selector     : 'horizontal-layout-1',
    templateUrl  : './layout-1.component.html',
    styleUrls    : ['./layout-1.component.scss'],
    encapsulation: ViewEncapsulation.None
})
export class HorizontalLayout1Component implements OnInit, OnDestroy
{
    fuseConfig: any;
    navigation: FuseNavigation;

    // Private
    private _unsubscribeAll: Subject<any>;

    /**
     * Constructor
     *
     * @ param {FuseConfigService} _fuseConfigService
     */
    constructor(
        private _fuseConfigService: FuseConfigService,
        private _store: Store<AppConfigurationState>
    ) {
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
            (navigation: FuseNavigation) => {
                (navigation) && (this.navigation = navigation);
        });

        // Subscribe to config changes
        this._fuseConfigService.config
            .pipe(takeUntil(this._unsubscribeAll))
            .subscribe((config) => {
                this.fuseConfig = config;
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
