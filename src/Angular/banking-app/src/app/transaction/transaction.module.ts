import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TransactionRoutingModule } from './transaction-routing.module';
import { TransactionComponent } from './transaction.component';
import { RegisterTransactionComponent } from './register-transaction/register-transaction.component';
import { ListTransactionComponent } from './list-transaction/list-transaction.component';


@NgModule({
  declarations: [
    TransactionComponent,
    RegisterTransactionComponent,
    ListTransactionComponent
  ],
  imports: [
    CommonModule,
    TransactionRoutingModule
  ]
})
export class TransactionModule { }
