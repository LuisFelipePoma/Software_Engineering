import { Component, OnInit } from '@angular/core'
import { TripsService } from '../../../../services/trips.service'
import { TripResponse } from '../../../../models/trip.model'
import { MatTableDataSource } from '@angular/material/table'

@Component({
  selector: 'app-trip-list',
  templateUrl: './trip-list.component.html',
  styleUrls: ['./trip-list.component.css']
})
export class TripListComponent implements OnInit {
  listTrips!: TripResponse[]
  filteredTrips!: TripResponse[]
  displayedColumns = ['date', 'time', 'origin', 'destination', 'route']
  dataSource = new MatTableDataSource<TripResponse>()
  date!: Date
  route!: string

  constructor (private tripsService: TripsService) {}

  ngOnInit (): void {
    this.tripsService.getAllTrips().subscribe(
      trips => {
        this.listTrips = trips
        this.filteredTrips = this.listTrips
        this.dataSource.data = this.filteredTrips
        console.log('Trips obtained:', trips)
      },
      error => {
        console.error('Error obtaining trips:', error)
      }
    )
  }

  filter () {
    let tripsByRoute: TripResponse[] = []
    let tripsByDate: TripResponse[] = []
    this.tripsService.filterByRoute(this.route).subscribe(
      trips => {
        tripsByRoute = trips
      },
      error => {
        console.error('Error obtaining trips:', error)
      }
    )

    this.tripsService
      .filterByDate(this.date ? this.date.toString() : '')
      .subscribe(
        trips => {
          tripsByDate = trips
          //
          if (tripsByRoute.length === 0 && tripsByDate.length === 0) {
            this.dataSource.data = []
            return
          }
          if (tripsByRoute.length === 0) {
            this.dataSource.data = tripsByDate
            return
          }
          if (tripsByDate.length === 0) {
            this.dataSource.data = tripsByRoute
            return
          }
          this.dataSource.data = tripsByRoute.filter(tripRoute => {
            return tripsByDate.some(tripDate => {
              return tripRoute.id === tripDate.id
            })
          })
        },
        error => {
          console.error('Error obtaining trips:', error)
        }
      )
  }
  // filter () {
  //   this.filteredTrips = this.listTrips.filter(trip => {
  //     return (
  //       trip.route.toLowerCase().includes(this.route.toLowerCase()) &&
  //       (this.date ? trip.date === this.date : true)
  //     )
  //   })
  //   this.dataSource.data = this.filteredTrips
  //   this.filteredTrips = this.listTrips
  // }

  clearFilters () {
    this.filteredTrips = this.listTrips
    this.dataSource.data = this.listTrips
  }
}
