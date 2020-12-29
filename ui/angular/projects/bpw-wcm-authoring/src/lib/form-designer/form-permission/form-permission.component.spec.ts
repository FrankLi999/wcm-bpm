import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FormPermissionComponent } from './form-permission.component';

describe('FormPermissionComponent', () => {
  let component: FormPermissionComponent;
  let fixture: ComponentFixture<FormPermissionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FormPermissionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FormPermissionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
