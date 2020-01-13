import { AfterViewInit, ChangeDetectorRef, Component, ElementRef, ViewChild } from '@angular/core';

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
  @ViewChild('footerView', { static: false }) footerView: ElementRef;
  constructor(private cd: ChangeDetectorRef) {}   
  changeDynamicComponent() {
    this.dynamicComponent = (this.dynamicComponent === 'dynamic-content') ? 'dynamic-content2' : 'dynamic-content';
    this.footerContent = (this.dynamicComponent === 'dynamic-content') ? 
      '<strong>This should be rendered in footer selection of ng-content.</strong> <dynamic-content></dynamic-content>' :
      'This should be rendered in footer selection of ng-content. <dynamic-content2></dynamic-content2>'; 
      // this.footerView.nativeElement.innerHTML = this.footerContent;
    console.log(this.footerView.nativeElement.innerHTML);
    console.log(this.footerContent);
    this.cd.detectChanges();
  }

  ngAfterViewInit() {
    console.log(this.footerView.nativeElement.innerHTML);
    // this.footerView.nativeElement.innerHTML = 'This should be rendered in footer selection of ng-content. <dynamic-content2></dynamic-content2>';
    this.footerContent = 'This should be rendered in footer selection of ng-content. <dynamic-content2></dynamic-content2>';
    this.cd.detectChanges();
  }
}
