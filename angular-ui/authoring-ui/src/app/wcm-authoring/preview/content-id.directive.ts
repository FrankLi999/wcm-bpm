import { Directive, Input, ElementRef } from '@angular/core';

@Directive({
  selector: '[contentId]'
})
export class ContentIdDirective {
  @Input("contentId") contentId: string;
  constructor(private elementRef: ElementRef) { 
  }

  // getContentId() {
  //   return this.contentId;
  // }
  // ngAfterViewInit(): void {
  //    console.log("directve init--" + this.contentId);  
  // }
}
