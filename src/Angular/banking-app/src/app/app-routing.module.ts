import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [{ path: 'transaction', loadChildren: () => import('./transaction/transaction.module').then(m => m.TransactionModule) }, { path: 'account', loadChildren: () => import('./account/account.module').then(m => m.AccountModule) }, { path: 'user', loadChildren: () => import('./user/user.module').then(m => m.UserModule) }, { path: 'report', loadChildren: () => import('./report/report.module').then(m => m.ReportModule) }];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
