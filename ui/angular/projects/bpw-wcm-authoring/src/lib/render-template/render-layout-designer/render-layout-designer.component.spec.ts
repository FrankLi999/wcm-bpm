import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RenderLayoutDesignerComponent } from './render-layout-designer.component';

describe('RenderLayoutDesignerComponent', () => {
  let component: RenderLayoutDesignerComponent;
  let fixture: ComponentFixture<RenderLayoutDesignerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RenderLayoutDesignerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RenderLayoutDesignerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
