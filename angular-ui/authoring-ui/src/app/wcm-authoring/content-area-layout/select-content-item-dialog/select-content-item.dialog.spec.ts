import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectContentItemDialog } from './select-content-item.dialog';

describe('SelectContentItemDialogComponent', () => {
  let component: SelectContentItemDialog;
  let fixture: ComponentFixture<SelectContentItemDialog>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SelectContentItemDialog ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectContentItemDialog);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
