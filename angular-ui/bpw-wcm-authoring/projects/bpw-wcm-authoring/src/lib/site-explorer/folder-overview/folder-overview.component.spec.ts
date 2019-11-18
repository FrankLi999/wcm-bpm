import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FolderOverviewComponent } from './folder-overview.component';

describe('FolderOverviewComponent', () => {
  let component: FolderOverviewComponent;
  let fixture: ComponentFixture<FolderOverviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FolderOverviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FolderOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
