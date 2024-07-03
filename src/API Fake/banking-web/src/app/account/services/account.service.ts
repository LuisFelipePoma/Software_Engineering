
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { AccountRequest } from '../interfaces/account-request.interface';
import { AccountResponse } from '../interfaces/account-response.interface';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  private baseUrl = environment.apiUrl; // URL base del backend

  constructor(private http: HttpClient) { }

  // Método para registrar una nueva cuenta
  registerAccount(newAccount: AccountRequest): Observable<AccountResponse> {
    const url = `${this.baseUrl}/accounts`; // Endpoint del backend para registrar cuentas
    return this.http.post<AccountRequest>(url, newAccount);
  }

  // Método para obtener todas las cuentas registradas (ejemplo)
  getAllAccounts(customerId: number): Observable<AccountResponse[]> {
    const url = `${this.baseUrl}/accounts/customers/${customerId}`; 
    return this.http.get<AccountResponse[]>(url);
  }

}
