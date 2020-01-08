import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DynamicContent2Component } from './dynamic-content2.component';

describe('DynamicContent2Component', () => {
  let component: DynamicContent2Component;
  let fixture: ComponentFixture<DynamicContent2Component>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DynamicContent2Component ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DynamicContent2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
