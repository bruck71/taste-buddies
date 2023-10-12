export class NewEventDTO {

    location: string;
    searchRadius: string;
    partySize: string;
    eventName: string;
    mealTime: Date;

    constructor(
        location: string,
        searchRadius: string,
        partySize: string,
        eventName: string, 
        mealTime: Date,
    ) {  }

}
