import { FormControl, FormGroup, Validators } from '@angular/forms';

export class UserFormFactory {

  static createInfoForm() {
    return new FormGroup({
      loginUser: new FormControl(''),
      name: new FormControl('', Validators.required),
      document: new FormControl('', [
        Validators.required,
        Validators.pattern('^[0-9]+$')
      ])
    });
  }

  static createPasswordForm() {
    return new FormGroup({
      oldPassword: new FormControl('', Validators.required),
      newPassword: new FormControl('', [
        Validators.required,
        Validators.minLength(6)
      ]),
      confirmPassword: new FormControl('', Validators.required)
    });
  }

}