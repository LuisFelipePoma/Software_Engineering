import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TransactionRoutingModule } from './transaction-routing.module';
import { TransactionComponent } from './transaction.component';
import { RegisterTransactionComponent } from './register-transaction/register-transaction.component';
import { ListTransactionComponent } from './list-transaction/list-transaction.component';
import { DetailTransactionComponent } from './detail-transaction/detail-transaction.component';
import { MaterialModule } from '../material/material.module';


@NgModule({
  declarations: [
    TransactionComponent,
    RegisterTransactionComponent,
    ListTransactionComponent,
    DetailTransactionComponent
  ],
  imports: [
    CommonModule,
    TransactionRoutingModule,
    MaterialModule
  ]
})
export class TransactionModule { }
