import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SiteExplorerComponent } from './site-explorer.component';

describe('SiteExplorerComponent', () => {
  let component: SiteExplorerComponent;
  let fixture: ComponentFixture<SiteExplorerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SiteExplorerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SiteExplorerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
