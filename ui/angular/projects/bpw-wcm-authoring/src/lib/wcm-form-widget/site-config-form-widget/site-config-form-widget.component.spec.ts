import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SiteConfigFormWidgetComponent } from './site-config-form-widget.component';

describe('SiteConfigFormWidgetComponent', () => {
  let component: SiteConfigFormWidgetComponent;
  let fixture: ComponentFixture<SiteConfigFormWidgetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SiteConfigFormWidgetComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SiteConfigFormWidgetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
