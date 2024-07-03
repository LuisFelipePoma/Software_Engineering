import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TransactionComponent } from './transaction.component';
import { DetailTransactionComponent } from './detail-transaction/detail-transaction.component';

const routes: Routes = [
  { path: '', component: TransactionComponent },
  { path: 'details/:id', component: DetailTransactionComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TransactionRoutingModule { }
