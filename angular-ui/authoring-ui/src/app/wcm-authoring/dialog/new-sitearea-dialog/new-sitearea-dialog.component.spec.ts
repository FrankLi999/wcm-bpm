import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewSiteareaDialogComponent } from './new-sitearea-dialog.component';

describe('NewSiteareaDialogComponent', () => {
  let component: NewSiteareaDialogComponent;
  let fixture: ComponentFixture<NewSiteareaDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewSiteareaDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewSiteareaDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
