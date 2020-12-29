import { async, ComponentFixture, TestBed } from "@angular/core/testing";

import { AuthoringTemplateListComponent } from "./authoring-template-list.component";

describe("AuthoringTemplateListComponent", () => {
  let component: AuthoringTemplateListComponent;
  let fixture: ComponentFixture<AuthoringTemplateListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AuthoringTemplateListComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AuthoringTemplateListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
