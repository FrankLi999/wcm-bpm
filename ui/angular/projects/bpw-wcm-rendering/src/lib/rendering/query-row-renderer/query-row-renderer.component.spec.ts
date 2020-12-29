import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { QueryRowRendererComponent } from './query-row-renderer.component';

describe('QueryRowRendererComponent', () => {
  let component: QueryRowRendererComponent;
  let fixture: ComponentFixture<QueryRowRendererComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ QueryRowRendererComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(QueryRowRendererComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
