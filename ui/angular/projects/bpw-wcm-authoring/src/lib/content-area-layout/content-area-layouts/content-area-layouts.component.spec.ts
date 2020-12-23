import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentAreaLayoutsComponent } from './content-area-layouts.component';

describe('ContentAreaLayoutsComponent', () => {
  let component: ContentAreaLayoutsComponent;
  let fixture: ComponentFixture<ContentAreaLayoutsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContentAreaLayoutsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContentAreaLayoutsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
