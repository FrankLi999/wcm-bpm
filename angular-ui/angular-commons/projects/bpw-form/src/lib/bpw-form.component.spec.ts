import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BpwFormComponent } from './bpw-form.component';

describe('BpwFormComponent', () => {
  let component: BpwFormComponent;
  let fixture: ComponentFixture<BpwFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BpwFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BpwFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
