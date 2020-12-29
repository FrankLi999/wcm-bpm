import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkflowFormWidgetComponent } from './workflow-form-widget.component';

describe('WorkflowFormWidgetComponent', () => {
  let component: WorkflowFormWidgetComponent;
  let fixture: ComponentFixture<WorkflowFormWidgetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WorkflowFormWidgetComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkflowFormWidgetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
