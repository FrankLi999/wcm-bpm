import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkflowTreeComponent } from './workflow-tree.component';

describe('WorkflowTreeComponent', () => {
  let component: WorkflowTreeComponent;
  let fixture: ComponentFixture<WorkflowTreeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WorkflowTreeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkflowTreeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
