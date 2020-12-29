import { async, ComponentFixture, TestBed } from "@angular/core/testing";

import { AuthoringTemplatePermissionComponent } from "./authopring-template-permission.component";

describe("AuthoringTemplatePermissionComponent", () => {
  let component: AuthoringTemplatePermissionComponent;
  let fixture: ComponentFixture<AuthoringTemplatePermissionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AuthoringTemplatePermissionComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AuthoringTemplatePermissionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
