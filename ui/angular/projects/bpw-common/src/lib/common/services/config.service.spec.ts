import { UIConfigService, WCM_UI_CONFIG } from "./config.service";
import { CommonModule } from "@angular/common";
import { Router } from "@angular/router";
import { RouterTestingModule } from "@angular/router/testing";
import { TestBed } from "@angular/core/testing";
import { UIConfig } from "bpw-common";
import { Platform } from "@angular/cdk/platform";

describe("UIConfigService", () => {
  let service: UIConfigService;
  class FakeAndroidPlatform extends Platform {
    ANDROID: boolean = true;
    IOS: boolean = false;
  }

  class FakeIOSPlatform extends Platform {
    ANDROID: boolean = false;
    IOS: boolean = true;
  }

  let appLayoutConfig: UIConfig = {
    // Color themes can be defined in src/app/app.theme.scss
    colorTheme: "theme-default",
    customScrollbars: true,
    layout: {
      title: "horizontal-layout",
      mode: "fullwidth",
      navbar: {
        primaryBackground: "wcm-light-200",
        secondaryBackground: "wcm-light-300",
        folded: false,
        display: false,
        position: "top",
        variant: "vertical",
      },
      toolbar: {
        customBackgroundColor: false,
        background: "wcm-light-300",
        display: false,
        position: "above",
      },
      footer: {
        customBackgroundColor: true,
        background: "wcm-light-300",
        display: false,
        position: "below-static",
      },
      leftSidePanel: {
        display: false,
      },
      rightSidePanel: {
        display: false,
      },
    },
  };
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CommonModule, RouterTestingModule],
      providers: [
        UIConfigService,
        {
          provide: WCM_UI_CONFIG,
          useValue: appLayoutConfig,
        },
      ],
    });
    service = TestBed.inject(UIConfigService);
  });

  it("should be created", () => {
    expect(service).toBeTruthy();
  });

  it("default config is defined", () => {
    expect(service.defaultConfig.colorTheme).toBe("theme-default");
  });

  it("set/get config work as expected", (done) => {
    service.config = { colorTheme: "theme-xxxx" };
    service.config.subscribe((c) => {
      expect(c.colorTheme).toBe("theme-xxxx");
      done();
    });
  });

  it("setConfig with emit option updated configuration", (done) => {
    service.setConfig({ colorTheme: "theme-xxxx" }, { emitEvent: true });
    service.getConfig().subscribe((c) => {
      expect(c.colorTheme).toBe("theme-xxxx");
      done();
    });
  });
  it("setConfig without emit option has no effect", (done) => {
    service.setConfig({ colorTheme: "theme-xxxx" }, { emitEvent: false });
    service.getConfig().subscribe((c) => {
      expect(c.colorTheme).toBe("theme-default");
      done();
    });
  });

  it("resetToDefaults resets to default configuration", (done) => {
    service.setConfig({ colorTheme: "theme-xxxx" }, { emitEvent: true });
    service.resetToDefaults();
    service.getConfig().subscribe((c) => {
      expect(c.colorTheme).toBe("theme-default");
      done();
    });
  });

  it("customScrollbars is false on Android", (done) => {
    let router = TestBed.inject(Router);
    let fakeAndroidPlatform = new FakeAndroidPlatform("andoid");
    let service = new UIConfigService(
      fakeAndroidPlatform,
      router,
      appLayoutConfig
    );
    service.getConfig().subscribe((c) => {
      expect(c.customScrollbars).toBeFalse();
      done();
    });
  });

  it("customScrollbars is false on IOS", (done) => {
    let router = TestBed.inject(Router);
    let fakeIosPlatform = new FakeIOSPlatform("ios");
    let service = new UIConfigService(fakeIosPlatform, router, appLayoutConfig);
    service.getConfig().subscribe((c) => {
      expect(c.customScrollbars).toBeFalse();
      done();
    });
  });
});
