import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WcmRendererComponent } from './wcm-renderer.component';

describe('WcmRendererComponent', () => {
  let component: WcmRendererComponent;
  let fixture: ComponentFixture<WcmRendererComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WcmRendererComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WcmRendererComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
