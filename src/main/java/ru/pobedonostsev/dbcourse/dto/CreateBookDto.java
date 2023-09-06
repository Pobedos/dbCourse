package ru.pobedonostsev.dbcourse.dto;

public class CreateBookDto {
    private String name;
    private AuthorDto genre;
    private AuthorDto[] authors;

    public String getName() {
        return name;
    }

    public CreateBookDto setName(String name) {
        this.name = name;
        return this;
    }

    public AuthorDto getGenre() {
        return genre;
    }

    public CreateBookDto setGenre(AuthorDto genre) {
        this.genre = genre;
        return this;
    }

    public AuthorDto[] getAuthors() {
        return authors;
    }

    public CreateBookDto setAuthors(AuthorDto[] authors) {
        this.authors = authors;
        return this;
    }
}
