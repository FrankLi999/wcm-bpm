import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CmmnModelerComponent } from './cmmn-modeler.component';

describe('CmmnModelerComponent', () => {
  let component: CmmnModelerComponent;
  let fixture: ComponentFixture<CmmnModelerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CmmnModelerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CmmnModelerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
