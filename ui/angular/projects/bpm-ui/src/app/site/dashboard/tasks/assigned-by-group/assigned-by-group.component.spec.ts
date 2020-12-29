import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignedByGroupComponent } from './assigned-by-group.component';

describe('AssignedByGroupComponent', () => {
  let component: AssignedByGroupComponent;
  let fixture: ComponentFixture<AssignedByGroupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AssignedByGroupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AssignedByGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
