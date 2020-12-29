import { Component, OnInit, ViewEncapsulation, Input } from "@angular/core";
import { wcmAnimations } from "../../animations/wcm-animations";

@Component({
  selector: "block-ui",
  templateUrl: "./block-ui.component.html",
  styleUrls: ["./block-ui.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations
})
export class BlockUIComponent implements OnInit {
  @Input() message: string = "Default Message";
  ngOnInit() {}
}
