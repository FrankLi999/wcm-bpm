import { Component, OnDestroy, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { Subject } from 'rxjs';
import { delay, filter, take, takeUntil } from 'rxjs/operators';
import { select, Store } from '@ngrx/store';
import { PerfectScrollbarDirective } from '../../../../../common/directives/perfect-scrollbar/perfect-scrollbar.directive';
import { Navigation } from '../../../../../common/types/navigation'; 
import { UIConfigService } from '../../../../../common/services/config.service'; 
import { NavigationService } from '../../../../../common/components/navigation/navigation.service';
import { SidebarService } from '../../../../../common/components/sidebar/sidebar.service';

import { AppConfigurationState } from '../../../../store/reducers/navigation.reducers';
import { getNavigation } from '../../../../store/selectors/navigation.selectors';

@Component({
    selector     : 'navbar-vertical-style-2',
    templateUrl  : './style-2.component.html',
    styleUrls    : ['./style-2.component.scss'],
    encapsulation: ViewEncapsulation.None
})
export class NavbarVerticalStyle2Component implements OnInit, OnDestroy
{
    uiConfig: any;
    navigation: Navigation[];

    // Private
    private _perfectScrollbar: PerfectScrollbarDirective;
    private _unsubscribeAll: Subject<any>;

    /**
     * Constructor
     *
     * @ param {UIConfigService} _uiConfigService
     * @ param {NavigationService} _navigationService
     * @ param {SidebarService} _sidebarService
     * @ param {Router} _router
     */
    constructor(
        private _uiConfigService: UIConfigService,
        private _navigationService: NavigationService,
        private _sidebarService: SidebarService,
        private _router: Router,
        private _store: Store<AppConfigurationState>
    )
    {
        // Set the private defaults
        this._unsubscribeAll = new Subject();
    }

    // -----------------------------------------------------------------------------------------------------
    // @ Accessors
    // -----------------------------------------------------------------------------------------------------

    // Directive
    @ViewChild(PerfectScrollbarDirective, {static: true})
    set directive(theDirective: PerfectScrollbarDirective) {
        if ( !theDirective )
        {
            return;
        }

        this._perfectScrollbar = theDirective;

        // Update the scrollbar on collapsable item toggle
        this._navigationService.onItemCollapseToggled
            .pipe(
                delay(500),
                takeUntil(this._unsubscribeAll)
            )
            .subscribe(() => {
                this._perfectScrollbar.update();
            });

        // Scroll to the active item position
        this._router.events
            .pipe(
                filter((event) => event instanceof NavigationEnd),
                take(1)
            )
            .subscribe(() => {
                    setTimeout(() => {
                        this._perfectScrollbar.scrollToElement('navbar .nav-link.active', -120);
                    });
                }
            );
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
        this._router.events
            .pipe(
                filter((event) => event instanceof NavigationEnd),
                takeUntil(this._unsubscribeAll)
            )
            .subscribe(() => {
                    if ( this._sidebarService.getSidebar('navbar') )
                    {
                        this._sidebarService.getSidebar('navbar').close();
                    }
                }
            );

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
    ngOnDestroy(): void {
        // Unsubscribe from all subscriptions
        this._unsubscribeAll.next();
        this._unsubscribeAll.complete();
    }

    // -----------------------------------------------------------------------------------------------------
    // @ Public methods
    // -----------------------------------------------------------------------------------------------------

    /**
     * Toggle sidebar opened status
     */
    toggleSidebarOpened(): void
    {
        this._sidebarService.getSidebar('navbar').toggleOpen();
    }

    /**
     * Toggle sidebar folded status
     */
    toggleSidebarFolded(): void
    {
        this._sidebarService.getSidebar('navbar').toggleFold();
    }
}
