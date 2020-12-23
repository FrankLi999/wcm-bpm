import { Component, Inject, OnDestroy, OnInit } from "@angular/core";

import { Locale } from "bpw-common";

import { navigation } from "./navigation/navigation";
import { locale as navigationEnglish } from "./navigation/i18n/en";
import { locale as navigationTurkish } from "./navigation/i18n/tr";

@Component({
  selector: "bpw-app",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"],
})
export class AppComponent {
  navigation: any = navigation;
  translations: Locale[] = [navigationEnglish, navigationTurkish];
  langs: string[] = ["en", "tr"];
  /**
   * Constructor
   */
  constructor() {}
}
