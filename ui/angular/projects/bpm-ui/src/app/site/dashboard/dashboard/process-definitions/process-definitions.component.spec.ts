import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcessDefinitionsComponent } from './process-definitions.component';

describe('ProcessDefinitionsComponent', () => {
  let component: ProcessDefinitionsComponent;
  let fixture: ComponentFixture<ProcessDefinitionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProcessDefinitionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProcessDefinitionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
