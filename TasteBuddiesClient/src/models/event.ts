import { User } from "./user";

export class Event {

    id: number;
    entryCode: string; // six characters. Numbers too?
    location: string; // zip code? Address?
    searchRadius: string;  // meters?
    partySize: string;
    eventName: string;
    restaurants: Array<{id: string}>;
    currentUser: User;
    otherUsers: Array<User>;
    mealTime: Date;
    photos: Array<{
        photo_reference: string;
        html_attributions: Array<string>;
        height: number;
        width: number;
    }>

    constructor(
        id: number,
        location: string,
        date: Date,
        searchRadius: string,
        partySize: string,
        eventName: string, 
        restaurants: Array<{id: string}>,
        entryCode: string,
        currentUser: User,
        otherUsers: Array<User>,
        photos: Array<{
            photo_reference: string;
            html_attributions: Array<string>;
            height: number;
            width: number;
        }>
        ) {
            this.id = id;
            this.location = location;
            this.mealTime = date;
            this.searchRadius = searchRadius;
            this.partySize = partySize;
            this.eventName = eventName;
            this.restaurants = restaurants;
            this.entryCode = entryCode;
            this.currentUser = currentUser;
            this.otherUsers = otherUsers;
            this.photos = photos;
        }

}
