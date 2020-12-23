import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomizablePageLayoutComponent } from './customizable-page-layout.component';

describe('CustomizablePageLayoutComponent', () => {
  let component: CustomizablePageLayoutComponent;
  let fixture: ComponentFixture<CustomizablePageLayoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CustomizablePageLayoutComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomizablePageLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
