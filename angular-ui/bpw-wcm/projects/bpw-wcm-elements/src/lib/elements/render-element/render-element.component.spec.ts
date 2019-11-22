import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RenderElementComponent } from './render-element.component';

describe('RenderElementComponent', () => {
  let component: RenderElementComponent;
  let fixture: ComponentFixture<RenderElementComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RenderElementComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RenderElementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
