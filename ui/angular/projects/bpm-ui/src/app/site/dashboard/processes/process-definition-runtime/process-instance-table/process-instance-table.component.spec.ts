import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcessInstanceTableComponent } from './process-instance-table.component';

describe('ProcessInstanceTableComponent', () => {
  let component: ProcessInstanceTableComponent;
  let fixture: ComponentFixture<ProcessInstanceTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProcessInstanceTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProcessInstanceTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
