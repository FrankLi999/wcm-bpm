import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HumanTasksComponent } from './human-tasks.component';

describe('HumanTasksComponent', () => {
  let component: HumanTasksComponent;
  let fixture: ComponentFixture<HumanTasksComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HumanTasksComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HumanTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
