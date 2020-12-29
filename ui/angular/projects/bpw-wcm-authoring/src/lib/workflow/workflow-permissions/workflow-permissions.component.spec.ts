import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkflowPermissionsComponent } from './workflow-permissions.component';

describe('WorkflowPermissionsComponent', () => {
  let component: WorkflowPermissionsComponent;
  let fixture: ComponentFixture<WorkflowPermissionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WorkflowPermissionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkflowPermissionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
