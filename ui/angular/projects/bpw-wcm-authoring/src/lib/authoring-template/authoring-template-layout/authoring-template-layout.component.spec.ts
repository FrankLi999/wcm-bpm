import { async, ComponentFixture, TestBed } from "@angular/core/testing";

import { AuthoringTemplateLayoutComponent } from "./authoring-template-layout.component";

describe("AuthoringTemplateLayoutComponent", () => {
  let component: AuthoringTemplateLayoutComponent;
  let fixture: ComponentFixture<AuthoringTemplateLayoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AuthoringTemplateLayoutComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AuthoringTemplateLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
