import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentAreaLayoutPermissionsComponent } from './content-area-layout-permissions.component';

describe('ContentAreaLayoutPermissionsComponent', () => {
  let component: ContentAreaLayoutPermissionsComponent;
  let fixture: ComponentFixture<ContentAreaLayoutPermissionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContentAreaLayoutPermissionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContentAreaLayoutPermissionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
