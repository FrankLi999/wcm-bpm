import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RenderPropertyComponent } from './render-property.component';

describe('RenderPropertyComponent', () => {
  let component: RenderPropertyComponent;
  let fixture: ComponentFixture<RenderPropertyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RenderPropertyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RenderPropertyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
