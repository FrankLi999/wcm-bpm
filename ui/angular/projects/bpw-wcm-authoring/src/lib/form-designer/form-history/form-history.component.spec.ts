import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FormHistoryComponent } from './form-history.component';

describe('FormHistoryComponent', () => {
  let component: FormHistoryComponent;
  let fixture: ComponentFixture<FormHistoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FormHistoryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FormHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
