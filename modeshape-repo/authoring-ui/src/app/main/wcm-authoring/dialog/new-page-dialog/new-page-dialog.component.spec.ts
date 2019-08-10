import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewPageDialogComponent } from './new-page-dialog.component';

describe('NewPageDialogComponent', () => {
  let component: NewPageDialogComponent;
  let fixture: ComponentFixture<NewPageDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewPageDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewPageDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
