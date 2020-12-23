import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WcmResponseComponent } from './wcm-response.component';

describe('WcmResponseComponent', () => {
  let component: WcmResponseComponent;
  let fixture: ComponentFixture<WcmResponseComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WcmResponseComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WcmResponseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
