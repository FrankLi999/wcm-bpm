import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentAreaDesignerComponent } from './content-area-designer.component';

describe('ContentAreaDesignerComponent', () => {
  let component: ContentAreaDesignerComponent;
  let fixture: ComponentFixture<ContentAreaDesignerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContentAreaDesignerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContentAreaDesignerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
