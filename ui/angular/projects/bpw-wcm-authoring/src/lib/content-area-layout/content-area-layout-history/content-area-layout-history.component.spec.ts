import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentAreaLayoutHistoryComponent } from './content-area-layout-history.component';

describe('ContentAreaLayoutHistoryComponent', () => {
  let component: ContentAreaLayoutHistoryComponent;
  let fixture: ComponentFixture<ContentAreaLayoutHistoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContentAreaLayoutHistoryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContentAreaLayoutHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
