import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventHomeComponent } from './event-home.component';

describe('eventhomeComponent', () => {
  let component: EventHomeComponent;
  let fixture: ComponentFixture<EventHomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EventHomeComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
