import { Component, OnInit } from '@angular/core';
import { User } from 'src/models/user';
import { UserService } from 'src/services/user.service';
import { Event } from 'src/models/event';
import { ActivatedRoute } from '@angular/router';
import { Observable, switchMap } from 'rxjs';
import { EventService } from 'src/services/event.service';

@Component({
  selector: 'app-event-home',
  templateUrl: './event-home.component.html',
  styleUrls: ['./event-home.component.css']
})
export class EventHomeComponent implements OnInit {

  upcommingEvents: Array<Event> = [];
  pastEvents: Array<Event> = [];
  // note: trailer $ is a convention for Observables in ng
  events$: Observable<Event[]>;
  selectedID: number;

  constructor(
    private route: ActivatedRoute,
    private eventService: EventService,
    ) {
      this.eventService.getEvents().subscribe({
        next: res => {
          for (let event of res) {
            if (Number.parseInt(event.mealTime) > new Date().getTime()) {
              this.upcommingEvents.push(event)
            } else {
              this.pastEvents.push(event)
            }
          }
        },
        error: e => {
          console.error(e);
        }
      })

  }

  ngOnInit(): void {
    this.events$ = this.route.paramMap.pipe(
      switchMap(params => {
        this.selectedID = Number(params.get('id'));
        return this.eventService.getEvents();
      })
    )
  }

}
