import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SiteTreeComponent } from './site-tree.component';

describe('SiteTreeComponent', () => {
  let component: SiteTreeComponent;
  let fixture: ComponentFixture<SiteTreeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SiteTreeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SiteTreeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
