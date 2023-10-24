export class Friendship {

    id: number;
    user1: string;
    user2: string;
    status: string;
    requestedBy: string;
    establishedDate: Date;
    actionUser: string;

    constructor(
        user1: string,
        user2: string,
        status: string,
        requestedBy: string,
        establishedDate: Date,
        actionUser: string
    ) {
        this.user1 = user1;
        this.user2 = user2;
        this.status = status;
        this.requestedBy = requestedBy;
        this.establishedDate = establishedDate;
        this.actionUser = actionUser;
    }
}