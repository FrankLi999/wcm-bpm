import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ValidationRuleHistoryComponent } from './validation-rule-history.component';

describe('ValidationRuleHistoryComponent', () => {
  let component: ValidationRuleHistoryComponent;
  let fixture: ComponentFixture<ValidationRuleHistoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ValidationRuleHistoryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ValidationRuleHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
