import { Directive, ElementRef } from '@angular/core';

@Directive({
    selector: '[widgetToggle]'
})
export class WidgetToggleDirective
{
    /**
     * Constructor
     *
     * @ param {ElementRef} elementRef
     */
    constructor(
        public elementRef: ElementRef
    )
    {
    }
}
