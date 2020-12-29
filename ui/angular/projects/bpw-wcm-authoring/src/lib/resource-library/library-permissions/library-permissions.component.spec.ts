import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LibraryPermissionsComponent } from './library-permissions.component';

describe('LibraryPermissionsComponent', () => {
  let component: LibraryPermissionsComponent;
  let fixture: ComponentFixture<LibraryPermissionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LibraryPermissionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LibraryPermissionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
