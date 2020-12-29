import { Component, OnInit, Input } from "@angular/core";
import { FormGroup } from "@angular/forms";

@Component({
  selector: "registration-step3",
  templateUrl: "./registration-step3.component.html",
  styleUrls: ["./registration-step3.component.scss"],
})
export class RegistrationStep3Component implements OnInit {
  constructor() {}

  @Input() regForm: FormGroup;
  formSubmitted: boolean = false;

  ngOnInit() {}

  submit() {
    console.log("submitted");
    console.log(this.regForm.value);
    this.formSubmitted = true;
  }
}
