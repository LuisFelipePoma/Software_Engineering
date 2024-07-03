export interface TripResponse {
  id: number
  date: Date
  time: Date
  origin: string
  destination: string
  route: string
}

export interface TripRequest {
  date: Date
  time: Date
  origin: string
  destination: string
  route: string
}
