CREATE TABLE Genre
(
    id   serial      NOT NULL PRIMARY KEY,
    name varchar(50) NOT NULL UNIQUE
);

CREATE TABLE Author
(
    id   serial      NOT NULL PRIMARY KEY,
    name varchar(50) NOT NULL UNIQUE
);

CREATE TABLE Book
(
    id       serial      NOT NULL PRIMARY KEY,
    genre_id bigint      NOT NULL,
    name     varchar(50) NOT NULL,
    CONSTRAINT "FK_genre_id" FOREIGN KEY (genre_id) REFERENCES Genre (id)
);

CREATE TABLE book_author
(
    book_id   bigint NOT NULL,
    author_id bigint NOT NULL,
    CONSTRAINT FK_book_id FOREIGN KEY (book_id) REFERENCES Book (id),
    CONSTRAINT FK_author_id FOREIGN KEY (author_id) REFERENCES Author (id)
);

CREATE TABLE BookState
(
    id    serial      NOT NULL PRIMARY KEY,
    state varchar(50) NOT NULL
);

CREATE TABLE BookInstance
(
    id       serial NOT NULL PRIMARY KEY,
    book_id  bigint NOT NULL,
    state_id bigint NOT NULL,
    CONSTRAINT FK_12 FOREIGN KEY (book_id) REFERENCES Book (id),
    CONSTRAINT FK_12_1 FOREIGN KEY (state_id) REFERENCES BookState (id)
);

CREATE TABLE Role
(
    id   serial      NOT NULL PRIMARY KEY,
    name varchar(50) NOT NULL UNIQUE
);

CREATE TABLE Users
(
    id         serial       NOT NULL PRIMARY KEY,
    firstName  varchar(50)  NOT NULL,
    lastName   varchar(50)  NOT NULL,
    patronymic varchar(50)  NOT NULL,
    role       bigint       NOT NULL,
    number     varchar(50)  NOT NULL,
    login      varchar(50)  NOT NULL UNIQUE,
    password   varchar(255) NOT NULL,
    request boolean default false not null,
    salary bigint default 0 not null,
    CONSTRAINT FK_10 FOREIGN KEY (role) REFERENCES Role (id)
);

CREATE TABLE Tokens
(
    id           serial       NOT NULL PRIMARY KEY,
    user_id      bigint       NOT NULL UNIQUE,
    refreshToken varchar(255) NOT NULL,
    CONSTRAINT FK_tokens_user FOREIGN KEY (user_id) REFERENCES Users (id)
);

CREATE TABLE Status
(
    id   serial      NOT NULL PRIMARY KEY,
    name varchar(50) NOT NULL
);

CREATE TABLE Review
(
    id     serial NOT NULL PRIMARY KEY,
    client bigint NOT NULL,
    text   text   NOT NULL,
    book   bigint NOT NULL,
    CONSTRAINT FK_8 FOREIGN KEY (book) REFERENCES Book (id),
    CONSTRAINT FK_9 FOREIGN KEY (client) REFERENCES Users (id)
);

CREATE TABLE Event
(
    id   serial                   NOT NULL PRIMARY KEY,
    date timestamp with time zone NOT NULL,
    host bigint                   NOT NULL,
    name varchar(50)              NOT NULL,
    CONSTRAINT FK_11 FOREIGN KEY (host) REFERENCES Users (id)
);

CREATE TABLE event_book
(
    event_id bigint NOT NULL,
    book_id  bigint NOT NULL,
    CONSTRAINT FK_15 FOREIGN KEY (event_id) REFERENCES Event (id),
    CONSTRAINT FK_16 FOREIGN KEY (book_id) REFERENCES BookInstance (id)
);

CREATE TABLE Log
(
    id        serial    NOT NULL PRIMARY KEY,
    client_id bigint    NOT NULL,
    book_id   bigint    NOT NULL,
    status_id bigint    NOT NULL,
    timestamp timestamp NOT NULL,
    CONSTRAINT FK_6 FOREIGN KEY (status_id) REFERENCES Status (id),
    CONSTRAINT FK_9_1 FOREIGN KEY (client_id) REFERENCES Users (id),
    CONSTRAINT FK_16_1 FOREIGN KEY (book_id) REFERENCES BookInstance (id)
);

CREATE TABLE Participants
(
    event_id bigint NOT NULL,
    user_id  bigint NOT NULL,
    CONSTRAINT FK_13 FOREIGN KEY (event_id) REFERENCES Event (id),
    CONSTRAINT FK_14 FOREIGN KEY (user_id) REFERENCES Users (id)
);
