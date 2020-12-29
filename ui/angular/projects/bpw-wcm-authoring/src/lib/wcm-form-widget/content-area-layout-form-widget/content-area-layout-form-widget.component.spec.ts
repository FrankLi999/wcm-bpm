import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentAreaLayoutFormWidgetComponent } from './content-area-layout-form-widget.component';

describe('ContentAreaLayoutFormWidgetComponent', () => {
  let component: ContentAreaLayoutFormWidgetComponent;
  let fixture: ComponentFixture<ContentAreaLayoutFormWidgetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContentAreaLayoutFormWidgetComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContentAreaLayoutFormWidgetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
