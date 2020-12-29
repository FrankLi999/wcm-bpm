import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentApprovalComponent } from './content-approval.component';

describe('ContentApprovalComponent', () => {
  let component: ContentApprovalComponent;
  let fixture: ComponentFixture<ContentApprovalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContentApprovalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContentApprovalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
