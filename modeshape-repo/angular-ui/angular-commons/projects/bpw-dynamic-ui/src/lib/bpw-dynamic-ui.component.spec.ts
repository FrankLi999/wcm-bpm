import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BpwDynamicUiComponent } from './bpw-dynamic-ui.component';

describe('BpwDynamicUiComponent', () => {
  let component: BpwDynamicUiComponent;
  let fixture: ComponentFixture<BpwDynamicUiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BpwDynamicUiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BpwDynamicUiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
