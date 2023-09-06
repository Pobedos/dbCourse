import {IUser} from "../models/IUser";

export enum Role {
    CLIENT,
    LIBRARIAN,
    DIRECTOR,
}

export function fromUser(user: IUser): Role {
    switch (user.role) {
        case "ROLE_CLIENT":
            return Role.CLIENT
        case "ROLE_LIBRARIAN":
            return Role.LIBRARIAN
        case "ROLE_DIRECTOR":
            return Role.DIRECTOR
        default:
            return {} as Role
    }
}