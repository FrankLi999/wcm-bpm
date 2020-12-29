import { DebugElement } from "@angular/core";
import { By } from "@angular/platform-browser";
import { async, ComponentFixture, TestBed } from "@angular/core/testing";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { BlockUIComponent } from "./block-ui.component";
import { ComponentFixtureAutoDetect } from "@angular/core/testing";

describe("BlockUIComponent", () => {
  let component: BlockUIComponent;
  let fixture: ComponentFixture<BlockUIComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [BlockUIComponent],
      imports: [MatProgressSpinnerModule],
      providers: [{ provide: ComponentFixtureAutoDetect, useValue: true }],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BlockUIComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
    const element: HTMLElement = fixture.nativeElement;
    const msgNode = element.querySelector(
      ".block-ui-wrapper .block-ui-message"
    );
    expect(msgNode.textContent).toContain("Default Message");
  });

  it("mat-spinner create", () => {
    expect(component).toBeTruthy();
    const element: HTMLElement = fixture.nativeElement;
    const spinnerNode = element.querySelector(
      // ".block-ui-spinner .spinner-container mat-spinner"
      "mat-spinner"
    );
    expect(spinnerNode).toBeDefined();
  });

  it("show message", () => {
    component.message = "blocking UI message";
    fixture.detectChanges();
    const element: DebugElement = fixture.debugElement;
    const msgNode = element.query(
      By.css(".block-ui-wrapper .block-ui-message")
    );
    expect(msgNode.nativeElement.textContent).toContain("blocking UI message");
  });
});
