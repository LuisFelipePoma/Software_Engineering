import { Component } from '@angular/core'
import { Form, FormBuilder, FormGroup, Validators } from '@angular/forms'
import { MatSnackBar } from '@angular/material/snack-bar'
import { TripsService } from '../../../../services/trips.service'
import { TripRequest } from '../../../../models/trip.model'

@Component({
  selector: 'app-trip-form',
  templateUrl: './trip-form.component.html',
  styleUrls: ['./trip-form.component.css']
})
export class TripFormComponent {
  form: FormGroup

  constructor (
    private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private tripsService: TripsService
  ) {
    this.form = this.fb.group({
      date: ['', [Validators.required]],
      time: ['', [Validators.required]],
      origin: ['', [Validators.required]],
      destination: ['', [Validators.required]],
      route: ['', [Validators.required]]
    })
  }
  controlHasError (control: string, error: string) {
    return this.form.controls[control].hasError(error)
  }

  register () {
    if (this.form.invalid) {
      return
    }

    const tripData: TripRequest = this.form.value

    this.tripsService.createTrip(tripData).subscribe({
      next: () => {
        this.showSnackBar('Trip registered')
        // Clean form
        this.form.reset()
        // avoid form to be dirty
        Object.keys(this.form.controls).forEach(key => {
          this.form.controls[key].setErrors(null)
        })
      },
      error: error => {
        console.error('Error registering trip:', error)
        this.showSnackBar('Error registering trip. Please, try again.')
      }
    })
  }

  private showSnackBar (message: string): void {
    this.snackBar.open(message, 'Cerrar', {
      duration: 3000
    })
  }
}
