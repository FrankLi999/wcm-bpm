import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TreeviewDropdownComponent } from './treeview-dropdown.component';

describe('TreeviewDropdownComponent', () => {
  let component: TreeviewDropdownComponent;
  let fixture: ComponentFixture<TreeviewDropdownComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TreeviewDropdownComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TreeviewDropdownComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
