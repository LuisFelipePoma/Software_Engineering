import { Component } from '@angular/core';
import { AuthRequest } from '../interfaces/auth.interface';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { NgForm } from '@angular/forms';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  authRequest: AuthRequest = {};

  constructor(
    private router: Router,
    private authService: AuthService,
    private snackBar: MatSnackBar
  ) { }

  login(form: NgForm) {
    if (form.invalid) {
      return;
    }
    this.authService.authenticate(this.authRequest)
      .subscribe(profile => {
        this.snackBar.open(`Bienvenido ${profile.firstName}`, 'Cerrar', {
          duration: 5000,
          verticalPosition: 'bottom',
          horizontalPosition: 'center'
        });
        this.router.navigate(['home']);
      });
  }

  //console.log(this.authRequest);


}
