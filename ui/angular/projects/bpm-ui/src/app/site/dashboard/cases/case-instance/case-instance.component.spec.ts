import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CaseInstanceComponent } from './case-instance.component';

describe('CaseInstanceComponent', () => {
  let component: CaseInstanceComponent;
  let fixture: ComponentFixture<CaseInstanceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CaseInstanceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CaseInstanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
