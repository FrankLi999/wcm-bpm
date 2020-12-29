import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SiteareaFormWidgetComponent } from './sitearea-form-widget.component';

describe('SiteareaFormWidgetComponent', () => {
  let component: SiteareaFormWidgetComponent;
  let fixture: ComponentFixture<SiteareaFormWidgetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SiteareaFormWidgetComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SiteareaFormWidgetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
