import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResourceTypeTreeComponent } from './resource-type-tree.component';

describe('ResourceTypeTreeComponent', () => {
  let component: ResourceTypeTreeComponent;
  let fixture: ComponentFixture<ResourceTypeTreeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ResourceTypeTreeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResourceTypeTreeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
