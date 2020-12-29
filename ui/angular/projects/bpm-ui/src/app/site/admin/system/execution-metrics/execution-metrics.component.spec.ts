import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExecutionMetricsComponent } from './execution-metrics.component';

describe('ExecutionMetricsComponent', () => {
  let component: ExecutionMetricsComponent;
  let fixture: ComponentFixture<ExecutionMetricsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExecutionMetricsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExecutionMetricsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
