import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ValidationRulePermissionsComponent } from './validation-rule-permissions.component';

describe('ValidationRulePermissionsComponent', () => {
  let component: ValidationRulePermissionsComponent;
  let fixture: ComponentFixture<ValidationRulePermissionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ValidationRulePermissionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ValidationRulePermissionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
