import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TripListComponent } from './components/trip-list/trip-list.component';
import { TripFormComponent } from './components/trip-form/trip-form.component';
import { TripReportComponent } from './components/trip-report/trip-report.component';


const routes: Routes = [
  { path: '', component: TripListComponent },
  { path: 'add', component:TripFormComponent },
  {path:'report', component:TripReportComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TripRoutingModule { }
