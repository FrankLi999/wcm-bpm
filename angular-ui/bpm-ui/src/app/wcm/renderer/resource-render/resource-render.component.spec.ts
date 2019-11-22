import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResourceRenderComponent } from './resource-render.component';

describe('ResourceRenderComponent', () => {
  let component: ResourceRenderComponent;
  let fixture: ComponentFixture<ResourceRenderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ResourceRenderComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResourceRenderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
