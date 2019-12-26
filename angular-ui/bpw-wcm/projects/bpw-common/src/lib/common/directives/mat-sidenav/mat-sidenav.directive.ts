import { Directive, Input, OnInit, HostListener, OnDestroy, HostBinding } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { MediaObserver } from '@angular/flex-layout';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { MatchMediaService } from '../../services/match-media.service';
import { MatSidenavHelperService } from './mat-sidenav.service';

@Directive({
    selector: '[matSidenavHelper]'
})
export class MatSidenavHelperDirective implements OnInit, OnDestroy {
    @HostBinding('class.mat-is-locked-open')
    isLockedOpen: boolean;

    @Input()
    matSidenavHelper: string;

    @Input()
    matIsLockedOpen: string;

    // Private
    private _unsubscribeAll: Subject<any>;

    /**
     * Constructor
     *
     * @ param {MatchMediaService} _matchMediaService
     * @ param {MatSidenavHelperService} _matSidenavHelperService
     * @ param {MatSidenav} _matSidenav
     * @ param {MediaObserver} _mediaObserver
     */
    constructor(
        private _matchMediaService: MatchMediaService,
        private _matSidenavHelperService: MatSidenavHelperService,
        private _matSidenav: MatSidenav,
        private _mediaObserver: MediaObserver
    ) {
        // Set the defaults
        this.isLockedOpen = true;

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
        // Register the sidenav to the service
        this._matSidenavHelperService.setSidenav(this.matSidenavHelper, this._matSidenav);

        if ( this.matIsLockedOpen && this._mediaObserver.isActive(this.matIsLockedOpen)) {
            this.isLockedOpen = true;
            this._matSidenav.mode = 'side';
            this._matSidenav.toggle(true);
        } else {
            this.isLockedOpen = false;
            this._matSidenav.mode = 'over';
            this._matSidenav.toggle(false);
        }

        this._matchMediaService.onMediaChange
            .pipe(takeUntil(this._unsubscribeAll))
            .subscribe(() => {
                if ( this.matIsLockedOpen && this._mediaObserver.isActive(this.matIsLockedOpen)) {
                    this.isLockedOpen = true;
                    this._matSidenav.mode = 'side';
                    this._matSidenav.toggle(true);
                } else {
                    this.isLockedOpen = false;
                    this._matSidenav.mode = 'over';
                    this._matSidenav.toggle(false);
                }
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
}

@Directive({
    selector: '[matSidenavToggler]'
})
export class MatSidenavTogglerDirective {
    @Input()
    matSidenavToggler: string;

    /**
     * Constructor
     *
     * @ param {MatSidenavHelperService} _matSidenavHelperService
     */
    constructor(
        private _matSidenavHelperService: MatSidenavHelperService) { }

    // -----------------------------------------------------------------------------------------------------
    // @ Public methods
    // -----------------------------------------------------------------------------------------------------

    /**
     * On click
     */
    @HostListener('click')
    onClick(): void {
        this._matSidenavHelperService.getSidenav(this.matSidenavToggler).toggle();
    }
}
