import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CmmnViewerComponent } from './cmmn-viewer.component';

describe('CmmnViewerComponent', () => {
  let component: CmmnViewerComponent;
  let fixture: ComponentFixture<CmmnViewerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CmmnViewerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CmmnViewerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
