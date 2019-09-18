import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BpwComponentsComponent } from './bpw-components.component';

describe('BpwComponentsComponent', () => {
  let component: BpwComponentsComponent;
  let fixture: ComponentFixture<BpwComponentsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BpwComponentsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BpwComponentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
