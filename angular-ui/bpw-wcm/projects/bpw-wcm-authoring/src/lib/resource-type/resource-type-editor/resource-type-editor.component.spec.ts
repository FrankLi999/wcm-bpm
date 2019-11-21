import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResourceTypeEditorComponent } from './resource-type-editor.component';

describe('ResourceTypeEditorComponent', () => {
  let component: ResourceTypeEditorComponent;
  let fixture: ComponentFixture<ResourceTypeEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ResourceTypeEditorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResourceTypeEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
