import { async, ComponentFixture, TestBed } from "@angular/core/testing";
import { RenderFragementComponent } from "./render-fragement.component";

describe("RenderFragementComponent", () => {
  let component: RenderFragementComponent;
  let fixture: ComponentFixture<RenderFragementComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [RenderFragementComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RenderFragementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
