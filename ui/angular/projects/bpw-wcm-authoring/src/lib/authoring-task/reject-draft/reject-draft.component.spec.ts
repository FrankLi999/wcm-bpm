import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RejectDraftComponent } from './reject-draft.component';

describe('RejectDraftComponent', () => {
  let component: RejectDraftComponent;
  let fixture: ComponentFixture<RejectDraftComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RejectDraftComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RejectDraftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
