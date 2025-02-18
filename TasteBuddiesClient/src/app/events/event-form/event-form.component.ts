import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NewEventDTO } from 'src/models/DTO/new-event-dto';
import { EventService } from 'src/services/event.service';

@Component({
  selector: 'app-event-form',
  templateUrl: './event-form.component.html',
  styleUrls: ['./event-form.component.css']
})
export class EventFormComponent implements OnInit {
  partySize: number[] = [2, 3, 4, 5, 6, 7, 8, 9, 10];
  newEvent: NewEventDTO = new NewEventDTO('63108', '2', '2', 'Saint Louis Event', new Date());

  constructor(
    private router: Router,
    private eventService: EventService,
    ) { }

  ngOnInit(): void {}

  onSubmit(): void {
    // Encode the User's location for uri construction
    const encodedLocation = encodeURIComponent(this.newEvent.location);
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
