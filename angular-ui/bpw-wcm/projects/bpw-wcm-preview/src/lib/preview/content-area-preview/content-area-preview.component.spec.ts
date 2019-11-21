import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentAreaPreviewComponent } from './content-area-preview.component';

describe('ContentAreaPreviewComponent', () => {
  let component: ContentAreaPreviewComponent;
  let fixture: ComponentFixture<ContentAreaPreviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContentAreaPreviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContentAreaPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
