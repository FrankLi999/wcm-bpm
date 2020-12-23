import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CaseDefinitionsComponent } from './case-definitions.component';

describe('CaseDefinitionsComponent', () => {
  let component: CaseDefinitionsComponent;
  let fixture: ComponentFixture<CaseDefinitionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CaseDefinitionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CaseDefinitionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
