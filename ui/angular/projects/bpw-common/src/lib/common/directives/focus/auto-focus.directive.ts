import { Directive, AfterViewInit, ElementRef } from "@angular/core";

@Directive({
  selector: "[autoFocus]",
})
export class AutoFocusDirective implements AfterViewInit {
  constructor(private host: ElementRef) {}

  ngAfterViewInit() {
    setTimeout(() => {
      this.host.nativeElement.focus();
    }, 500);
  }
}
