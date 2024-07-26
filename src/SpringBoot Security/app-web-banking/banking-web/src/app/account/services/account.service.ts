import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { environment } from '../../../environments/environment';
import { AccountResponse } from '../interfaces/account-response.interface';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private http = inject(HttpClient);

  email: string = 'hmendo81@gmail.com';

  getAccountsByUser(): Observable<AccountResponse[]> {
    return this.http.post<AccountResponse[]>(`${environment.apiUrl}/admin/accounts/user?email=${this.email}`, {});
  }

  
}
