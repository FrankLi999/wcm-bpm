import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RenderTemplateHistoryComponent } from './render-template-history.component';

describe('RenderTemplateHistoryComponent', () => {
  let component: RenderTemplateHistoryComponent;
  let fixture: ComponentFixture<RenderTemplateHistoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RenderTemplateHistoryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RenderTemplateHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
