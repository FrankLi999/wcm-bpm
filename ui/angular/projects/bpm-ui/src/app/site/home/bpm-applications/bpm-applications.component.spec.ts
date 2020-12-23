import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BpmApplicationsComponent } from './bpm-applications.component';

describe('BpmApplicationsComponent', () => {
  let component: BpmApplicationsComponent;
  let fixture: ComponentFixture<BpmApplicationsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BpmApplicationsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BpmApplicationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
