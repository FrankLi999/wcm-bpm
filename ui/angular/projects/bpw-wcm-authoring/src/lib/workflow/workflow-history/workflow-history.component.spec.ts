import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkflowHistoryComponent } from './workflow-history.component';

describe('WorkflowHistoryComponent', () => {
  let component: WorkflowHistoryComponent;
  let fixture: ComponentFixture<WorkflowHistoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WorkflowHistoryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkflowHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
