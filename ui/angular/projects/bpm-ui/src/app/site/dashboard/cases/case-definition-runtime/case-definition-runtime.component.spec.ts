import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CaseDefinitionRuntimeComponent } from './case-definition-runtime.component';

describe('CaseDefinitionRuntimeComponent', () => {
  let component: CaseDefinitionRuntimeComponent;
  let fixture: ComponentFixture<CaseDefinitionRuntimeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CaseDefinitionRuntimeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CaseDefinitionRuntimeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
