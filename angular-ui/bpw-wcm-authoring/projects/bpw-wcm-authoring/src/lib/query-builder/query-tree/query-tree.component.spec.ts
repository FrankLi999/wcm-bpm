import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { QueryTreeComponent } from './query-tree.component';

describe('QueryTreeComponent', () => {
  let component: QueryTreeComponent;
  let fixture: ComponentFixture<QueryTreeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ QueryTreeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(QueryTreeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
