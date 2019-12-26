import { AfterViewInit, ChangeDetectorRef, Component, OnDestroy, OnInit, QueryList, ViewChildren, ViewEncapsulation } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { 
  wcmAnimations,
  PerfectScrollbarDirective,
  SidebarService
} from 'bpw-common';

import { AcademyCourseService } from '../course.service';

@Component({
    selector     : 'academy-course',
    templateUrl  : './course.component.html',
    styleUrls    : ['./course.component.scss'],
    encapsulation: ViewEncapsulation.None,
    animations   : wcmAnimations
})
export class AcademyCourseComponent implements OnInit, OnDestroy, AfterViewInit
{
    animationDirection: 'left' | 'right' | 'none';
    course: any;
    courseStepContent: any;
    currentStep: number;

    @ViewChildren(PerfectScrollbarDirective)
    scrollbarDirectives: QueryList<PerfectScrollbarDirective>;

    // Private
    private _unsubscribeAll: Subject<any>;

    /**
     * Constructor
     *
     * @param {AcademyCourseService} _academyCourseService
     * @param {ChangeDetectorRef} _changeDetectorRef
     * @param {SidebarService} _sidebarService
     */
    constructor(
        private _academyCourseService: AcademyCourseService,
        private _changeDetectorRef: ChangeDetectorRef,
        private _sidebarService: SidebarService
    )
    {
        // Set the defaults
        this.animationDirection = 'none';
        this.currentStep = 0;

        // Set the private defaults
        this._unsubscribeAll = new Subject();
    }

    // -----------------------------------------------------------------------------------------------------
    // @ Lifecycle hooks
    // -----------------------------------------------------------------------------------------------------

    /**
     * On init
     */
    ngOnInit(): void
    {
        // Subscribe to courses
        this._academyCourseService.onCourseChanged
            .pipe(takeUntil(this._unsubscribeAll))
            .subscribe(course => {
                this.course = course;
            });
    }

    /**
     * After view init
     */
    ngAfterViewInit(): void
    {
        this.courseStepContent = this.scrollbarDirectives.find((scrollbarDirective) => {
            return scrollbarDirective.elementRef.nativeElement.id === 'course-step-content';
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

    // -----------------------------------------------------------------------------------------------------
    // @ Public methods
    // -----------------------------------------------------------------------------------------------------

    /**
     * Go to step
     *
     * @param step
     */
    gotoStep(step): void
    {
        // Decide the animation direction
        this.animationDirection = this.currentStep < step ? 'left' : 'right';

        // Run change detection so the change
        // in the animation direction registered
        this._changeDetectorRef.detectChanges();

        // Set the current step
        this.currentStep = step;
    }

    /**
     * Go to next step
     */
    gotoNextStep(): void
    {
        if ( this.currentStep === this.course.totalSteps - 1 )
        {
            return;
        }

        // Set the animation direction
        this.animationDirection = 'left';

        // Run change detection so the change
        // in the animation direction registered
        this._changeDetectorRef.detectChanges();

        // Increase the current step
        this.currentStep++;
    }

    /**
     * Go to previous step
     */
    gotoPreviousStep(): void
    {
        if ( this.currentStep === 0 )
        {
            return;
        }

        // Set the animation direction
        this.animationDirection = 'right';

        // Run change detection so the change
        // in the animation direction registered
        this._changeDetectorRef.detectChanges();

        // Decrease the current step
        this.currentStep--;
    }

    /**
     * Toggle the sidebar
     *
     * @param name
     */
    toggleSidebar(name): void
    {
        this._sidebarService.getSidebar(name).toggleOpen();
    }
}
