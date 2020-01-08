import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements AfterViewInit {
  name = 'Angular';
  show = true;
  dynamicComponent = 'dynamic-content';
  footerContent = 'This should be rendered in footer selection of ng-content.';
  @ViewChild('footer', { static: false }) footer: ElementRef;

  changeDynamicComponent() {
    this.dynamicComponent = (this.dynamicComponent === 'dynamic-content') ? 'dynamic-content2' : 'dynamic-content';
    this.footerContent = (this.dynamicComponent === 'dynamic-content') ? 
      '<strong>This should be rendered in footer selection of ng-content.</strong> <dynamic-content></dynamic-content>' :
      'This should be rendered in footer selection of ng-content. <dynamic-content2></dynamic-content2>'; 
    console.log(this.footer.nativeElement.innerHTML);
    console.log(this.footerContent);
  }

  ngAfterViewInit() {
    console.log(this.footer.nativeElement.innerHTML);
  }
}
