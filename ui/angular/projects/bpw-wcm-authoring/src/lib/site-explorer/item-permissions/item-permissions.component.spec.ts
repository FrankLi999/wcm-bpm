import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemPermissionsComponent } from './item-permissions.component';

describe('ItemPermissionsComponent', () => {
  let component: ItemPermissionsComponent;
  let fixture: ComponentFixture<ItemPermissionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ItemPermissionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ItemPermissionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
