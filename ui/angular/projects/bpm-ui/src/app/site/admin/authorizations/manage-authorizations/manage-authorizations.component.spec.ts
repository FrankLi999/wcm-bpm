import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageAuthorizationsComponent } from './manage-authorizations.component';

describe('ManageAuthorizationsComponent', () => {
  let component: ManageAuthorizationsComponent;
  let fixture: ComponentFixture<ManageAuthorizationsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ManageAuthorizationsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageAuthorizationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
