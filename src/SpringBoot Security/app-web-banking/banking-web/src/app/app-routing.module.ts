import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';

const routes: Routes = [
  { path: '', loadChildren: () => import('./user/user.module').then(m => m.UserModule) }, 
  {path: 'home', component: HomeComponent },
  { path: 'transaction', loadChildren: () => import('./transaction/transaction.module').then(m => m.TransactionModule) }, 
  { path: 'account', loadChildren: () => import('./account/account.module').then(m => m.AccountModule) }, 
  { path: 'report', loadChildren: () => import('./report/report.module').then(m => m.ReportModule) }];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
