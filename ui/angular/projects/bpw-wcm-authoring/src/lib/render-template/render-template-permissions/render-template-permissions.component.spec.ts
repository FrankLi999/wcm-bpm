import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RenderTemplatePermissionsComponent } from './render-template-permissions.component';

describe('RenderTemplatePermissionsComponent', () => {
  let component: RenderTemplatePermissionsComponent;
  let fixture: ComponentFixture<RenderTemplatePermissionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RenderTemplatePermissionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RenderTemplatePermissionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
