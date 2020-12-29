import { Directive, Input, ElementRef } from '@angular/core';

@Directive({
  selector: '[contentId]'
})
export class ContentIdDirective {
  @Input("contentId") contentId: string;
  constructor(private elementRef: ElementRef) { 
  }
}
