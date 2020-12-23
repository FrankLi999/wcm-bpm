import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectAuthoringTemplateDialogComponent } from './select-authoring-template-dialog.component';

describe('SelectAuthoringTemplateDialogComponent', () => {
  let component: SelectAuthoringTemplateDialogComponent;
  let fixture: ComponentFixture<SelectAuthoringTemplateDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SelectAuthoringTemplateDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectAuthoringTemplateDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
