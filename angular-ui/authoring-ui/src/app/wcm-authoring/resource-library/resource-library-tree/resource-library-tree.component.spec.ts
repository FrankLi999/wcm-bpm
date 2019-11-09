import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResourceLibraryTreeComponent } from './resource-library-tree.component';

describe('ResourceLibraryTreeComponent', () => {
  let component: ResourceLibraryTreeComponent;
  let fixture: ComponentFixture<ResourceLibraryTreeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ResourceLibraryTreeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResourceLibraryTreeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
