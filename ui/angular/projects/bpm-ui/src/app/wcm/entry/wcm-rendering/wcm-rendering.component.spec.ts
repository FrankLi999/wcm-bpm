import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WcmRenderingComponent } from './wcm-rendering.component';

describe('WcmRenderingComponent', () => {
  let component: WcmRenderingComponent;
  let fixture: ComponentFixture<WcmRenderingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WcmRenderingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WcmRenderingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
