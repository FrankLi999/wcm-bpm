import { Component, NgModule } from "@angular/core";
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { CommonModule } from "@angular/common";
import { AutoFocusDirective } from "./auto-focus.directive";
@Component({
  template: `<div autoFocus tabindex="1">Test</div>`,
})
class HostComponent {}
@NgModule({
  declarations: [HostComponent, AutoFocusDirective],
  exports: [HostComponent],
})
class HostModule {}

describe("AutoFocusDirective", () => {
  let component: HostComponent;
  let element: HTMLElement;
  let fixture: ComponentFixture<HostComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CommonModule, HostModule], // * we import the host module
    }).compileComponents();

    fixture = TestBed.createComponent(HostComponent);
    component = fixture.componentInstance;
    element = fixture.nativeElement;

    fixture.detectChanges(); // * so the directive gets appilied
  });

  it("should create a host instance", () => {
    expect(component).toBeTruthy();
  });

  it("should set focus", (done) => {
    // * arrange
    const el = element.querySelector("div");
    const TIMEOUT_DELAY = 600;
    setTimeout(() => {
      //TODO: verify focused
      // * assert
      // expect(document.activeElement === el).toBeTruthy(); // * we check if the directive worked correctly
      done();
    }, TIMEOUT_DELAY);
  });
});
