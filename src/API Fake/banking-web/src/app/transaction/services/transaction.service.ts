import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { TransactionResponse } from '../interfaces/transaction-response.interface';
import { catchError, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  // Método para obtener todas las transacciones
  getAllTransactions(): Observable<TransactionResponse[]> {
    const url = `${this.baseUrl}/transactions`;
    return this.http.get<TransactionResponse[]>(url);
  }

  getTransactionById(transactionId: number): Observable<TransactionResponse> {
    const url = `${this.baseUrl}/transactions/${transactionId}`;
    return this.http.get<TransactionResponse>(url);
  }

}
