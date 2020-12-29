import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CategoryPermissionsComponent } from './category-permissions.component';

describe('CategoryPermissionsComponent', () => {
  let component: CategoryPermissionsComponent;
  let fixture: ComponentFixture<CategoryPermissionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CategoryPermissionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CategoryPermissionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
