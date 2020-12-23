import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DecisionInstanceComponent } from './decision-instance.component';

describe('DecisionInstanceComponent', () => {
  let component: DecisionInstanceComponent;
  let fixture: ComponentFixture<DecisionInstanceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DecisionInstanceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DecisionInstanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
