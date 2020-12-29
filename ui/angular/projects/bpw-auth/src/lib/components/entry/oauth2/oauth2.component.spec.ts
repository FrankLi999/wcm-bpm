import { async, ComponentFixture, TestBed } from "@angular/core/testing";

import { OAuth2Component } from "./oauth2.component";

describe("OAuth2Component", () => {
  let component: OAuth2Component;
  let fixture: ComponentFixture<OAuth2Component>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [OAuth2Component,
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OAuth2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
