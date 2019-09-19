import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResourceTypeLayoutComponent } from './resource-type-layout.component';

describe('ResourceTypeLayoutComponent', () => {
  let component: ResourceTypeLayoutComponent;
  let fixture: ComponentFixture<ResourceTypeLayoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ResourceTypeLayoutComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResourceTypeLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
