import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CategoryFormWidgetComponent } from './category-form-widget.component';

describe('CategoryFormWidgetComponent', () => {
  let component: CategoryFormWidgetComponent;
  let fixture: ComponentFixture<CategoryFormWidgetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CategoryFormWidgetComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CategoryFormWidgetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
