import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DecisionDefinitionRuntimeComponent } from './decision-definition-runtime.component';

describe('DecisionDefinitionRuntimeComponent', () => {
  let component: DecisionDefinitionRuntimeComponent;
  let fixture: ComponentFixture<DecisionDefinitionRuntimeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DecisionDefinitionRuntimeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DecisionDefinitionRuntimeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
