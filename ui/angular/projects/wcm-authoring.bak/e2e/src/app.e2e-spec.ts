import { AppPage } from "./app.po";
import { browser, logging } from "protractor";

describe("workspace-project App", () => {
  let page: AppPage;

  beforeEach(() => {
    page = new AppPage();
  });

  it("should display title", () => {
    page.navigateTo();
    expect(browser.getTitle()).toEqual("Modeshape WCM - Authoring System");
  });

  it("should display welcome message", () => {
    page.navigateTo();
    // expect(page.getTitleText()).toEqual("Modeshape WCM - Authoring System");
    expect(page.getLoginTitle()).toContain("Welcome to the WCM System!");
  });

  afterEach(async () => {
    // Assert that there are no errors emitted from the browser
    const logs = await browser.manage().logs().get(logging.Type.BROWSER);
    expect(logs).not.toContain(
      jasmine.objectContaining({
        level: logging.Level.SEVERE,
      } as logging.Entry)
    );
  });
});
