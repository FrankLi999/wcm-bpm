import { async, ComponentFixture, TestBed } from "@angular/core/testing";

import { WcmPageComponent } from "./wc./wcm-page.component

describe("WcmPageComponent", () => {
  let component: WcmPageComponent;
  let fixture: ComponentFixture<WcmPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [WcmPageComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WcmPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
