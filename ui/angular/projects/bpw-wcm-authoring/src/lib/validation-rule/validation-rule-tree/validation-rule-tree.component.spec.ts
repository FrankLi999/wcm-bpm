import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ValidationRuleTreeComponent } from './validation-rule-tree.component';

describe('ValiationRuleTreeComponent', () => {
  let component: ValidationRuleTreeComponent;
  let fixture: ComponentFixture<ValidationRuleTreeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ValidationRuleTreeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ValidationRuleTreeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
