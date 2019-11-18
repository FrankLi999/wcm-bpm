import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RenderTemplatesComponent } from './render-templates.component';

describe('RenderTemplatesComponent', () => {
  let component: RenderTemplatesComponent;
  let fixture: ComponentFixture<RenderTemplatesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RenderTemplatesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RenderTemplatesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
