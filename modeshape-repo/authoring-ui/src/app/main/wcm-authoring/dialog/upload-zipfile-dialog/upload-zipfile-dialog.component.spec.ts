import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadZipfileDialogComponent } from './upload-zipfile-dialog.component';

describe('UploadZipfileDialogComponent', () => {
  let component: UploadZipfileDialogComponent;
  let fixture: ComponentFixture<UploadZipfileDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UploadZipfileDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UploadZipfileDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
