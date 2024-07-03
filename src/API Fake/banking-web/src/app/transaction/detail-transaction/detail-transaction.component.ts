import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TransactionResponse } from '../interfaces/transaction-response.interface';
import { TransactionService } from '../services/transaction.service';

@Component({
  selector: 'app-detail-transaction',
  templateUrl: './detail-transaction.component.html',
  styleUrls: ['./detail-transaction.component.css']
})
export class DetailTransactionComponent implements OnInit {

  transaction: TransactionResponse | undefined;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private transactionService: TransactionService
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const transactionId = +params['id'];
      this.getTransactionDetails(transactionId);
    });
  }

  getTransactionDetails(transactionId: number): void {
    this.transactionService.getTransactionById(transactionId).subscribe({
      next: (transaction: TransactionResponse) => {
        this.transaction = transaction;
        console.log('Detalles de la transacción:', transactionId);        
        console.log('Detalles de la transacción:', this.transaction);
      },
      error: (error) => {
        console.error('Error al obtener los detalles de la transacción:', error);
      }
    });
  }

  handleBack(): void {
    this.router.navigate(['/home']);
  }
}
