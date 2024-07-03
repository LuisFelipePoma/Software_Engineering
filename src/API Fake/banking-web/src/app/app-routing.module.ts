import { NgModule } from '@angular/core'
import { RouterModule, Routes } from '@angular/router'
import { HomeComponent } from './home/home.component'
import { authGuard } from './user/helpers/admin.guard'

const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('./user/user.module').then(m => m.UserModule)
  },
  { path: 'home', component: HomeComponent, canActivate: [authGuard] },
  {
    path: 'transaction',
    loadChildren: () =>
      import('./transaction/transaction.module').then(m => m.TransactionModule),
    canActivate: [authGuard]
  },
  {
    path: 'account',
    loadChildren: () =>
      import('./account/account.module').then(m => m.AccountModule),
    canActivate: [authGuard]
  }
]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
