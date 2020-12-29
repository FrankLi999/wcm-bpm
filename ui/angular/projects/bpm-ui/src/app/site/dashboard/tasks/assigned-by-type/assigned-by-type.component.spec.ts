import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignedByTypeComponent } from './assigned-by-type.component';

describe('AssignedByTypeComponent', () => {
  let component: AssignedByTypeComponent;
  let fixture: ComponentFixture<AssignedByTypeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AssignedByTypeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AssignedByTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
