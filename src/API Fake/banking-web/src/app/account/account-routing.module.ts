import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AccountComponent } from './account.component';
import { RegisterAccountComponent } from './components/register-account/register-account.component';
import { EditAccountComponent } from './components/edit-account/edit-account.component';
import { FilterAccountComponent } from './components/filter-account/filter-account.component';

const routes: Routes = [
  { path: '', component: AccountComponent },
  { path: 'register', component: RegisterAccountComponent },
  { path: 'edit', component: EditAccountComponent },
  { path: 'filter', component: FilterAccountComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AccountRoutingModule { }
