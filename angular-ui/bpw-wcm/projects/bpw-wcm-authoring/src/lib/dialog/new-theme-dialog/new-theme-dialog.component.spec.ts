import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewThemeDialogComponent } from './new-theme-dialog.component';

describe('NewThemeDialogComponent', () => {
  let component: NewThemeDialogComponent;
  let fixture: ComponentFixture<NewThemeDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewThemeDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewThemeDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
