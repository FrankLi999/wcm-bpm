import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { QueryResultColumnComponent } from './query-result-column.component';

describe('QueryResultColumnComponent', () => {
  let component: QueryResultColumnComponent;
  let fixture: ComponentFixture<QueryResultColumnComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ QueryResultColumnComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(QueryResultColumnComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
