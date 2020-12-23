import { async, ComponentFixture, TestBed } from "@angular/core/testing";

import { AuthoringTemplateEditorComponent } from "./authoring-template-editor.component";

describe("AuthoringTemplateEditorComponent", () => {
  let component: AuthoringTemplateEditorComponent;
  let fixture: ComponentFixture<AuthoringTemplateEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AuthoringTemplateEditorComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AuthoringTemplateEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
