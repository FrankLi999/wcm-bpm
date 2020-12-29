import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BpmnDiagramComponent } from './bpmn-diagram.component';

describe('BpmnDiagramComponent', () => {
  let component: BpmnDiagramComponent;
  let fixture: ComponentFixture<BpmnDiagramComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BpmnDiagramComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BpmnDiagramComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
