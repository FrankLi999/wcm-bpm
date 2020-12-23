import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OpenIncidentsComponent } from './open-incidents.component';

describe('OpenIncidentsComponent', () => {
  let component: OpenIncidentsComponent;
  let fixture: ComponentFixture<OpenIncidentsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OpenIncidentsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OpenIncidentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
