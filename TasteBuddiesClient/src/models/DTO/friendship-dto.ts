import { FriendsDTO } from "./friends-dto";

export class FriendshipDTO {
    id: number;
    user: FriendsDTO;
    friend: FriendsDTO;
    status: string;

    constructor(user: FriendsDTO, friend: FriendsDTO, status: string) {
        this.user = user;
        this.friend = friend;
        this.status = status;
    }
}