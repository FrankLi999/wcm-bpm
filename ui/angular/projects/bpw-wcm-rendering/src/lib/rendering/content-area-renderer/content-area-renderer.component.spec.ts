import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentAreaRendererComponent } from './content-area-renderer.component';

describe('ContentAreaRendererComponent', () => {
  let component: ContentAreaRendererComponent;
  let fixture: ComponentFixture<ContentAreaRendererComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContentAreaRendererComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContentAreaRendererComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
