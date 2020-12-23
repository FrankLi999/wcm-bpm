import { CopierService } from "./copier.service";
import { TestBed } from "@angular/core/testing";
describe("UIConfigService", () => {
  let service: CopierService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CopierService],
    });

    service = TestBed.get(CopierService); // * inject service instance
  });

  it("should be created", () => {
    expect(service).toBeTruthy();
  });
});
