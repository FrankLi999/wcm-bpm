import { Component } from '@angular/core';
import {ErrorStateMatcher} from '@angular/material';
import {FormBuilder, FormGroup, FormControl, FormGroupDirective, NgForm} from '@angular/forms';
import { RxwebValidators, ValidationAlphabetLocale } from '@rxweb/reactive-form-validators';

/** Error when the parent is invalid */
class CrossFieldErrorMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    return control.dirty && form.invalid;
  }
}

@Component({
  selector: 'material-form',
  templateUrl: './material.component.html',
  styleUrls: [ './material.component.scss' ]
})
export class MaterialComponent  {
  userForm: FormGroup;
  errorMatcher = new CrossFieldErrorMatcher();

  constructor(private fb: FormBuilder) {
    this.initForm();
  }

  initForm() {
    this.userForm = this.fb.group({
      username: ['', [RxwebValidators.alpha(), RxwebValidators.email()]],
      password: '',
      verifyPassword: ''
    }, {
      validator: this.passwordValidator
    })
  }

  passwordValidator(form: FormGroup) {
    const condition = form.get('password').value !== form.get('verifyPassword').value;

    return condition ? { passwordsDoNotMatch: true} : null;
  }
}
