import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TransferComponent } from './transfer.component';
import { CreateTransferComponent } from './create-transfer/create-transfer.component';

const routes: Routes = [
  { path: '', component: TransferComponent },
  { path: 'create-transfer', component: CreateTransferComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TransferRoutingModule { }
