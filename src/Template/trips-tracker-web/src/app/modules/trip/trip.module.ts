import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TripRoutingModule } from './trip-routing.module';

import { TripListComponent } from './components/trip-list/trip-list.component';
import { TripFormComponent } from './components/trip-form/trip-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../material/material.module';
import { TripReportComponent } from './components/trip-report/trip-report.component';


@NgModule({
  declarations: [
    
    TripListComponent,
    TripFormComponent,
    TripReportComponent
  ],
  imports: [
    CommonModule,
    TripRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialModule
  ]
})
export class TripModule { }
