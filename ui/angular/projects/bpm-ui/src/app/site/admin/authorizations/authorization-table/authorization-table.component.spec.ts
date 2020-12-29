import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthorizationTableComponent } from './authorization-table.component';

describe('AuthorizationTableComponent', () => {
  let component: AuthorizationTableComponent;
  let fixture: ComponentFixture<AuthorizationTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AuthorizationTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AuthorizationTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
