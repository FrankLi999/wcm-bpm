import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcessDefinitionRuntimeComponent } from './process-definition-runtime.component';

describe('ProcessDefinitionRuntimeComponent', () => {
  let component: ProcessDefinitionRuntimeComponent;
  let fixture: ComponentFixture<ProcessDefinitionRuntimeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProcessDefinitionRuntimeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProcessDefinitionRuntimeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
