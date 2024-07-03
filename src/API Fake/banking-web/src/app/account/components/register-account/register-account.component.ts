import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar'; // Importar MatSnackBar
import { AccountResponse } from '../../interfaces/account-response.interface';
import { AccountService } from '../../services/account.service';
import { accountNumberValidator, amountValidator } from '../../helpers/account-validators';
import { AccountRequest } from '../../interfaces/account-request.interface';
import { AuthService } from '../../../user/services/auth.service';
import { LoginResponse } from '../../../user/interfaces/login-response.interface';

@Component({
  selector: 'app-register-account',
  templateUrl: './register-account.component.html',
  styleUrls: ['./register-account.component.css']
})
export class RegisterAccountComponent implements OnInit {

  form: FormGroup;
  loginResponse: LoginResponse | null;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private accountService: AccountService,
    private authService: AuthService,
    private snackBar: MatSnackBar // Inyectar MatSnackBar
  ) {
    this.form = this.fb.group({
      accountNumber: ['', [Validators.required, accountNumberValidator]],
      balance: ['', [Validators.required, amountValidator]]
    });
    this.loginResponse = this.authService.getLoginResponse(); // Obtener LoginResponse del localStorage
  }

  ngOnInit(): void {
  }

  controlHasError(control: string, error: string): boolean {
    return this.form.controls[control].hasError(error);
  }

  onSubmit(): void {
    if (this.form.invalid || !this.loginResponse) {
      return;
    }

    const formValue = this.form.value;

    const newAccount: AccountRequest = {
      id: 0,
      accountNumber: formValue.accountNumber,
      balance: formValue.balance,
      customerId: this.loginResponse.user.id // Asignar customerId del LoginResponse al nuevo objeto de cuenta
    };

    this.accountService.registerAccount(newAccount).subscribe({
      next: (response: AccountResponse) => {
        this.router.navigate(['/account']);
        this.showSnackBar('Cuenta registrada exitosamente');
      },
      error: (error) => {
        console.error('Error al registrar cuenta:', error);
        this.showSnackBar('Error al registrar cuenta. Por favor, intenta de nuevo.');
      }
    });
  }

  private showSnackBar(message: string): void {
    this.snackBar.open(message, 'Cerrar', {
      duration: 3000, // Duración en milisegundos
    });
  }
}
