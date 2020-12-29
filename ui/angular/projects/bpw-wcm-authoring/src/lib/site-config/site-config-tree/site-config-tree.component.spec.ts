import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SiteConfigTreeComponent } from './site-config-tree.component';

describe('SiteConfigTreeComponent', () => {
  let component: SiteConfigTreeComponent;
  let fixture: ComponentFixture<SiteConfigTreeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SiteConfigTreeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SiteConfigTreeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
