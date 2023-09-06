package ru.pobedonostsev.dbcourse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ru.pobedonostsev.dbcourse.dto.AuthorDto;
import ru.pobedonostsev.dbcourse.dto.BookAuthors;
import ru.pobedonostsev.dbcourse.dto.BookDto;
import ru.pobedonostsev.dbcourse.dto.CreateBookDto;
import ru.pobedonostsev.dbcourse.dto.DoubleIdDto;
import ru.pobedonostsev.dbcourse.dto.IdDto;
import ru.pobedonostsev.dbcourse.dto.ReviewDto;
import ru.pobedonostsev.dbcourse.service.BookService;
import ru.pobedonostsev.framework.exception.DaoException;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/{id}")
    public BookAuthors getBook(@PathVariable Long id) throws DaoException {
        return bookService.getDetail(id);
    }

    @GetMapping("")
    public List<BookAuthors> getBooks() throws DaoException {
        return bookService.getAll();
    }

    @PostMapping
    public void createBook(@RequestBody CreateBookDto dto) throws DaoException {
        bookService.create(dto);
    }

    @GetMapping("/popular")
    public List<BookAuthors> getPopular() throws DaoException {
        return bookService.getPopular();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createBook(@RequestParam BookDto bookDto) throws DaoException {
        bookService.create(bookDto);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void delete(@RequestParam Long id) throws DaoException {
        bookService.delete(id);
    }

    @GetMapping("/user/{id}")
    public List<BookAuthors> getUserBooks(@PathVariable long id) {
        return bookService.getUserBooks(id);
    }

    @GetMapping("/authors")
    public List<AuthorDto> getAuthors() {
        return bookService.getAuthors();
    }

    @GetMapping("/genres")
    public List<AuthorDto> getGenres() {
        return bookService.getGenres();
    }

    @GetMapping("/states")
    public List<AuthorDto> getStates() {
        return bookService.getStates();
    }

    @GetMapping("/user/{id}/orders")
    public List<BookAuthors> getUserOrders(@PathVariable long id) {
        return bookService.getUserOrders(id);
    }

    @PostMapping("/return")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void returnBook(@RequestBody IdDto id) {
        bookService.returnBook(id.getId());
    }

    @PostMapping("/cancel")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void cancelBook(@RequestBody IdDto id) {
        bookService.returnBook(id.getId());
    }

    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void orderBook(@RequestBody DoubleIdDto id) {
        bookService.orderBook(id);
    }

    @PostMapping("/orders/approve")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void approveOrder(@RequestBody DoubleIdDto id) {
        bookService.approveOrder(id);
    }

    @GetMapping("/orders")
    public List<BookAuthors> getAllOrders() {
        return bookService.getAllOrders();
    }

    @GetMapping("/author/{id}")
    public List<BookAuthors> getAuthorBooks(@PathVariable long id) {
        return bookService.getAuthorBooks(id);
    }

    @GetMapping("/{id}/instances")
    public List<BookAuthors> getBookInstanceInHall(@PathVariable long id) {
        return bookService.getBookInstanceInHall(id);
    }

    @GetMapping("/{id}/reviews")
    public List<ReviewDto> getReviews(@PathVariable long id) {
        return bookService.getReviews(id);
    }

    @PostMapping("/reviews")
    public void addReview(@RequestBody ReviewDto reviewDto) {
        bookService.addReview(reviewDto);
    }

    @PostMapping("/instance")
    public void addInstance(@RequestBody BookAuthors bookDto) {
        bookService.addInstance(bookDto);
    }

    @PostMapping("/stock")
    public void getBookInstanceInHall(@RequestBody IdDto id) {
        bookService.bookToStock(id.getId());
    }

    @GetMapping("/event/{id}")
    public List<BookAuthors> getBooksOnEvent(@PathVariable long id) throws DaoException {
        return bookService.getUsersOnEvent(id);
    }

    @GetMapping("/hall")
    public List<BookAuthors> getHall() {
        return bookService.getHall();
    }
}
