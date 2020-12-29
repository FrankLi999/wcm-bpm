import { browser, by, element } from "protractor";

export class AppPage {
  navigateTo() {
    return browser.get(browser.baseUrl) as Promise<any>;
  }
  getLoginTitle() {
    // return element(by.css('app-root .content span')).getText() as Promise<string>;
    return element(by.css(".title")).getText() as Promise<string>;
  }

  async logit() {
    console.log(">>browser.getTitle()>>", await browser.getTitle());
    console.log(
      '.... by.xpath("/html/head/title")...',
      await element(by.xpath("/html/head/title"))
    );

    console.log(
      '.... by.tagName("head")...',
      await element(by.tageName("head")).isPresent()
    );

    console.log(
      '.... by.tagName("title")...',
      await element(by.tageName("title")).isPresent()
    );
  }
}
