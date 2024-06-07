import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TransferRoutingModule } from './transfer-routing.module';
import { TransferComponent } from './transfer.component';
import { CreateTransferComponent } from './create-transfer/create-transfer.component';
import { MaterialModule } from '../material/material.module';


@NgModule({
  declarations: [
    TransferComponent,
    CreateTransferComponent
  ],
  imports: [
    CommonModule,
    TransferRoutingModule,
    MaterialModule,
  ]
})
export class TransferModule { }
