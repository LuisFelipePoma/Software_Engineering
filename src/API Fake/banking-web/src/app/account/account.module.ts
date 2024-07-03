import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AccountRoutingModule } from './account-routing.module';
import { AccountComponent } from './account.component';
import { RegisterAccountComponent } from './components/register-account/register-account.component';
import { EditAccountComponent } from './components/edit-account/edit-account.component';
import { FilterAccountComponent } from './components/filter-account/filter-account.component';
import { MaterialModule } from '../material/material.module';
import { ReactiveFormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    AccountComponent,
    RegisterAccountComponent,
    EditAccountComponent,
    FilterAccountComponent
  ],
  imports: [
    CommonModule,
    AccountRoutingModule,
    ReactiveFormsModule,
    MaterialModule,
    
  ]
})
export class AccountModule { }
