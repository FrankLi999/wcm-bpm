import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewLibraryDialogComponent } from './new-library-dialog.component';

describe('NewLibraryDialogComponent', () => {
  let component: NewLibraryDialogComponent;
  let fixture: ComponentFixture<NewLibraryDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewLibraryDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewLibraryDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
