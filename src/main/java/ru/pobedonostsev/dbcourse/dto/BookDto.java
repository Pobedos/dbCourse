package ru.pobedonostsev.dbcourse.dto;

public class BookDto {
    private long bookId;
    private long genreId;
    private String name;

    public long getGenreId() {
        return genreId;
    }

    public String getName() {
        return name;
    }

    public BookDto setGenreId(long genreId) {
        this.genreId = genreId;
        return this;
    }

    public BookDto setName(String name) {
        this.name = name;
        return this;
    }

    public long getBookId() {
        return bookId;
    }

    public BookDto setBookId(long bookId) {
        this.bookId = bookId;
        return this;
    }
}
