import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SiteAreaComponent } from './site-area.component';

describe('SiteAreaComponent', () => {
  let component: SiteAreaComponent;
  let fixture: ComponentFixture<SiteAreaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SiteAreaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SiteAreaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
