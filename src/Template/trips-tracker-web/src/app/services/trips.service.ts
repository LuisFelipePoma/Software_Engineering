import { Injectable } from '@angular/core'
import { HttpClient } from '@angular/common/http'
import { environment } from '../../environments/environment'
import { map, Observable } from 'rxjs'
import { TripRequest, TripResponse } from '../models/trip.model'
import { TripReport } from '../models/trip-report.model'

@Injectable({
  providedIn: 'root'
})
export class TripsService {
  private apiUrl = `${environment.apiUrl}/trips`

  constructor (private http: HttpClient) {}

  getAllTrips (): Observable<TripResponse[]> {
    return this.http.get<TripResponse[]>(this.apiUrl)
  }

  filterByRoute (route: string): Observable<TripResponse[]> {
    return this.http.get<TripResponse[]>(
      `${this.apiUrl}/filter-by-route?route=${route}`
    )
  }

  filterByDate (date: string): Observable<TripResponse[]> {
    return this.http.get<TripResponse[]>(
      `${this.apiUrl}/filter-by-date?date=${date}`
    )
  }
  getReport (): Observable<TripReport[]> {
    return this.http.get<TripReport[]>(`${this.apiUrl}/stats-by-route`)
  }

  createTrip (request: TripRequest): Observable<TripResponse> {
    return this.http.post<TripResponse>(this.apiUrl, request)
  }
}
