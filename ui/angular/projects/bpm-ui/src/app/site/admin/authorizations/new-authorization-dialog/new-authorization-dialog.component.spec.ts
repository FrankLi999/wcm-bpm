import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewAuthorizationDialogComponent } from './new-authorization-dialog.component';

describe('NewAuthorizationDialogComponent', () => {
  let component: NewAuthorizationDialogComponent;
  let fixture: ComponentFixture<NewAuthorizationDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewAuthorizationDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewAuthorizationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
