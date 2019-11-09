import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RenderTemplateTreeComponent } from './render-template-tree.component';

describe('RenderTemplateTreeComponent', () => {
  let component: RenderTemplateTreeComponent;
  let fixture: ComponentFixture<RenderTemplateTreeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RenderTemplateTreeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RenderTemplateTreeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
