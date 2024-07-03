import { Component, OnInit } from '@angular/core'
import { TripsService } from '../../../../services/trips.service'
import { TripReport } from '../../../../models/trip-report.model'
import { MatTableDataSource } from '@angular/material/table'
import { TripResponse } from '../../../../models/trip.model'

@Component({
  selector: 'app-trip-report',
  templateUrl: './trip-report.component.html',
  styleUrls: ['./trip-report.component.css']
})
export class TripReportComponent implements OnInit {
  tripsReports: TripReport[] = []
  dataSource = new MatTableDataSource<TripResponse>()

  constructor (private tripsService: TripsService) {}

  ngOnInit (): void {
    this.tripsService.getReport().subscribe(
      reports => {
        console.log('Report obtained:', reports)
        this.tripsReports = reports
      },
      error => {
        console.error('Error obtaining report:', error)
      }
    )
  }

  getRouteInfo (route: string) {
    this.tripsService.filterByRoute(route).subscribe(
      trips => {
        this.dataSource.data = trips
      },
      error => {
        console.error('Error obtaining trips:', error)
      }
    )
  }
}
