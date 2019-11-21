import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SiteAreaLayoutComponent } from './site-area-layout.component';

describe('SiteAreaLayoutComponent', () => {
  let component: SiteAreaLayoutComponent;
  let fixture: ComponentFixture<SiteAreaLayoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SiteAreaLayoutComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SiteAreaLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
