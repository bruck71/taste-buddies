import { Component, OnInit, NgZone } from '@angular/core';
import { Router } from '@angular/router';
import { NewEventDTO } from 'src/models/DTO/new-event-dto';
import { EventService } from 'src/services/event.service';

declare var google: any;

@Component({
  selector: 'app-event-form',
  templateUrl: './event-form.component.html',
  styleUrls: ['./event-form.component.css']
})
export class EventFormComponent implements OnInit {
  partySize: number[] = [2, 3, 4, 5, 6, 7, 8, 9, 10];
  newEvent: NewEventDTO = new NewEventDTO('63108', '2', '2', 'Saint Louis Event', new Date());
  selectedLocation: string = '';

  constructor(
    private router: Router,
    private eventService: EventService,
    private ngZone: NgZone,
    ) { }

  ngOnInit(): void {
    const locationInput = document.getElementById('location-input');
    if (locationInput instanceof HTMLInputElement) {
      const autocomplete = new google.maps.places.Autocomplete(locationInput, { types: ['geocode'] });

      autocomplete.addListener('place_changed', () => {
        this.ngZone.run(() => {
          const place: google.maps.places.PlaceResult = autocomplete.getPlace();
          if (place.formatted_address) {
            this.selectedLocation = place.formatted_address;
          }
        });
      });
    } else {
      console.error("location input element not found.");
    }
  }


  onSubmit(): void {
    // Encode the User's location for uri construction
    const encodedLocation = encodeURIComponent(this.selectedLocation);
    // Update newEvent with encoded location
    this.newEvent.location = encodedLocation;

    console.log(this.newEvent)

    // copy the newEvent data so we can manipulate it without updating the template
    const formData = Object.assign(this.newEvent)
    formData.searchRadius = String(Number.parseFloat(this.newEvent.searchRadius) * 1609.344);
    formData.mealTime = new Date(this.newEvent.mealTime)
    

    this.eventService.createEvent(formData).subscribe({
      next: res => {
        this.router.navigate(['/event']);
      },
      error: (e) => { 
        console.error(e.message)
      }
    });
  }

  reloadPage(): void {
    window.location.reload();
  }
}
