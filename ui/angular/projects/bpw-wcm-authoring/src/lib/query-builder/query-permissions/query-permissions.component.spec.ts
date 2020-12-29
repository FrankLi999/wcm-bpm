import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { QueryPermissionsComponent } from './query-permissions.component';

describe('QueryPermissionsComponent', () => {
  let component: QueryPermissionsComponent;
  let fixture: ComponentFixture<QueryPermissionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ QueryPermissionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(QueryPermissionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
