import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewSiteConfigDialogComponent } from './new-site-config-dialog.component';

describe('NewSiteConfigDialogComponent', () => {
  let component: NewSiteConfigDialogComponent;
  let fixture: ComponentFixture<NewSiteConfigDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewSiteConfigDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewSiteConfigDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
