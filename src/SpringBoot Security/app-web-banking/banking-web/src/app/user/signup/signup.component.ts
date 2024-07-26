import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';


@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent {



  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private snackBar: MatSnackBar,
    private router: Router
  ) { }


  form: FormGroup = this.fb.group({
    firstName: [, [Validators.required]],
    lastName: [, [Validators.required]],
    email: [, [Validators.required, Validators.email]],
    password: [, [Validators.required, Validators.minLength(4)]],
  });
  errors: string[] = [];
  passwordVisible = false;
  

  controlHasError(control: string, error: string) {
    return this.form.controls[control].hasError(error);
  }
  
  signup() {
    if (this.form.invalid) {
      return;
    }
    const formValue = this.form.value;

    this.authService.signup(formValue)
      .subscribe({
        next: profile => {
          this.authService.authenticate({
            email: formValue.email,
            password: formValue.password
          })
            .subscribe(profile => {
              this.snackBar.open(`Bienvenido ${profile.firstName}`, 'Cerrar', {
                duration: 5000,
                verticalPosition: 'bottom',
                horizontalPosition: 'center'
              });
              this.router.navigate(['']);
            })
        },
        error: error => {
          if (error.error.status === 400) {
            this.errors.push(error.error.detail);
          } else if (error.error.status === 422) {
            this.errors.push(...error.error.errors);
          }
        }
      });
  }

}
