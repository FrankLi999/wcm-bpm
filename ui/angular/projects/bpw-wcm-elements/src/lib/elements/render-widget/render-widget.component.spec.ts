import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RenderWidgetComponent } from './render-widget.component';

describe('RenderWidgetComponent', () => {
  let component: RenderWidgetComponent;
  let fixture: ComponentFixture<RenderWidgetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RenderWidgetComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RenderWidgetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
