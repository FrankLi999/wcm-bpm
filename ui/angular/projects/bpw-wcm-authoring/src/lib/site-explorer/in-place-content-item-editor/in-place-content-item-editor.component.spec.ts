import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InPlaceContentItemEditorComponent } from './in-place-content-item-editor.component';

describe('InPlaceContentItemEditorComponent', () => {
  let component: InPlaceContentItemEditorComponent;
  let fixture: ComponentFixture<InPlaceContentItemEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InPlaceContentItemEditorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InPlaceContentItemEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
