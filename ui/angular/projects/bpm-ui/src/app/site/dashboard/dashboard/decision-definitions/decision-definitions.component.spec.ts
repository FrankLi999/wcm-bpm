import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DecisionDefinitionsComponent } from './decision-definitions.component';

describe('DecisionDefinitionsComponent', () => {
  let component: DecisionDefinitionsComponent;
  let fixture: ComponentFixture<DecisionDefinitionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DecisionDefinitionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DecisionDefinitionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
