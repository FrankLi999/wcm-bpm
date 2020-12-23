import { TestBed } from "@angular/core/testing";

import { RendererService } from "./renderer.service";

describe("RendererService", () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it("should be created", () => {
    const service: RendererService = TestBed.inject(RendererService);
    expect(service).toBeTruthy();
  });
});
