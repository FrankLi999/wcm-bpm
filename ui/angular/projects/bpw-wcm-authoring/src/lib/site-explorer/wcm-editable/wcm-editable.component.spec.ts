import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WcmEditableComponent } from './wcm-editable.component';

describe('WcmEditableComponent', () => {
  let component: WcmEditableComponent;
  let fixture: ComponentFixture<WcmEditableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WcmEditableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WcmEditableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
