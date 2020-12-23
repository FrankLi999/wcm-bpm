import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { QueryResultRendererComponent } from './query-result-renderer.component';

describe('QueryResultRendererComponent', () => {
  let component: QueryResultRendererComponent;
  let fixture: ComponentFixture<QueryResultRendererComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ QueryResultRendererComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(QueryResultRendererComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
