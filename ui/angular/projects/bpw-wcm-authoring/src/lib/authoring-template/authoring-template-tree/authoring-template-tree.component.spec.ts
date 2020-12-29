import { async, ComponentFixture, TestBed } from "@angular/core/testing";

import { AuthoringTemplateTreeComponent } from "./authoring-template-tree.component";

describe("ResourceTypeTreeComponent", () => {
  let component: AuthoringTemplateTreeComponent;
  let fixture: ComponentFixture<AuthoringTemplateTreeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AuthoringTemplateTreeComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AuthoringTemplateTreeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
