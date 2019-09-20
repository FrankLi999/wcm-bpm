import { browser, by, element } from 'protractor';

export class AppPage {
  navigateTo() {
    return browser.get(browser.baseUrl) as Promise<any>;
  }

  getTitleText() {
    return element(by.css('app .content span')).getText() as Promise<string>;
  }

  getAppRoot() {
    return element(by.css('app'));
  }
}
