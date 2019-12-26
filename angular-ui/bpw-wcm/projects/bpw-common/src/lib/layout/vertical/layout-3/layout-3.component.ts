import { Component, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { select, Store } from '@ngrx/store';
import { Navigation } from '../../../common/types/navigation'; 
import { UIConfigService } from '../../../common/services/config.service'; 
import { AppConfigurationState } from '../../store/reducers/navigation.reducers';
import { getNavigation } from '../../store/selectors/navigation.selectors';

@Component({
    selector     : 'vertical-layout-3',
    templateUrl  : './layout-3.component.html',
    styleUrls    : ['./layout-3.component.scss'],
    encapsulation: ViewEncapsulation.None
})
export class VerticalLayout3Component implements OnInit, OnDestroy
{
    uiConfig: any;
    navigation: Navigation[];

    // Private
    private _unsubscribeAll: Subject<any>;

    /**
     * Constructor
     *
     * @ param {UIConfigService} _uiConfigService
     */
    constructor(
        private _uiConfigService: UIConfigService,
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
            (navigation: Navigation[]) => {
                (navigation) && (this.navigation = navigation);
        });

        // Subscribe to config changes
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
