import { async, ComponentFixture, TestBed } from "@angular/core/testing";

import { AuthoringTemplateHistoryComponent } from "./authoring-template-history.component";

describe("AuthoringTemplateHistoryComponent", () => {
  let component: AuthoringTemplateHistoryComponent;
  let fixture: ComponentFixture<AuthoringTemplateHistoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AuthoringTemplateHistoryComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AuthoringTemplateHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
