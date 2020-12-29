import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentReviewTasksComponent } from './content-review-tasks.component';

describe('ContentReviewTasksComponent', () => {
  let component: ContentReviewTasksComponent;
  let fixture: ComponentFixture<ContentReviewTasksComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContentReviewTasksComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContentReviewTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
