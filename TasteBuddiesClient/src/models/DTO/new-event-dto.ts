export class NewEventDTO {

    location: string;
    searchRadius: string;
    eventName: String;
    mealTime: Date;

    constructor(
        location: string,
        searchRadius: string,
        eventName: String, 
        mealTime: Date,
    ) {  }

}
