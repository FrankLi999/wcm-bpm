import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BpwAuthComponent } from './bpw-auth.component';

describe('BpwAuthComponent', () => {
  let component: BpwAuthComponent;
  let fixture: ComponentFixture<BpwAuthComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BpwAuthComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BpwAuthComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
