import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WcmAuthoringComponent } from './wcm-authoring.component';

describe('WcmAuthoringComponent', () => {
  let component: WcmAuthoringComponent;
  let fixture: ComponentFixture<WcmAuthoringComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WcmAuthoringComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WcmAuthoringComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
