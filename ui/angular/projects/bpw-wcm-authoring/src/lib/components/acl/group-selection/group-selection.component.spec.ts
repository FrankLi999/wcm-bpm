import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupSelectionComponent } from './group-selection.component';

describe('GroupSelectionComponent', () => {
  let component: GroupSelectionComponent;
  let fixture: ComponentFixture<GroupSelectionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GroupSelectionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GroupSelectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
