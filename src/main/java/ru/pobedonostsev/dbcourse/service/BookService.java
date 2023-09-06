package ru.pobedonostsev.dbcourse.service;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.pobedonostsev.dbcourse.dao.book.BookDao;
import ru.pobedonostsev.dbcourse.dto.AuthorDto;
import ru.pobedonostsev.dbcourse.dto.BookAuthors;
import ru.pobedonostsev.dbcourse.dto.BookDto;
import ru.pobedonostsev.dbcourse.dto.CreateBookDto;
import ru.pobedonostsev.dbcourse.dto.DoubleIdDto;
import ru.pobedonostsev.dbcourse.dto.ReviewDto;
import ru.pobedonostsev.dbcourse.dto.UserDto;
import ru.pobedonostsev.dbcourse.dto.UserDtoWithoutPassword;
import ru.pobedonostsev.dbcourse.exception.BadRequestException;
import ru.pobedonostsev.dbcourse.model.Book;
import ru.pobedonostsev.dbcourse.model.User;
import ru.pobedonostsev.framework.dao.BaseDao;
import ru.pobedonostsev.framework.exception.DaoException;
import ru.pobedonostsev.framework.exception.EntityNotFoundException;
import ru.pobedonostsev.framework.service.BaseService;

@Service
public class BookService extends BaseService<Long, Book> {
    private static final Log log = LogFactory.getLog(BookService.class);
    @Autowired
    private BookDao bookDao;

    @Autowired
    private UserService userService;

    private static final Random random = new Random();

    @Override
    protected BaseDao<Long, Book> getDao() {
        return bookDao;
    }

    public void create(BookDto bookDto) throws DaoException {
        bookDao.store(new Book()
            .setName(bookDto.getName())
            .setGenreId(bookDto.getGenreId()));
    }

    public List<BookAuthors> getAll() {
        return bookDao.getAll();
    }

    public List<BookAuthors> getPopular() {
        return bookDao.getPopular();
    }

    public BookAuthors getDetail(Long id) throws DaoException {
        try {
            BookAuthors book = bookDao.getDetail(id);
            if (book == null) {
                throw new EntityNotFoundException("Book not found");
            }
            return book;
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    public List<BookAuthors> getUserBooks(long id) {
        return bookDao.getUserDto(id, "IN_HAND");
    }

    public List<BookAuthors> getUserOrders(long id) {
        return bookDao.getUserDto(id, "IN_ORDER");
    }

    public void returnBook(Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        bookDao.insertWithState(id, 3, user.getPK());
    }

    public List<BookAuthors> getAuthorBooks(long id) {
        return bookDao.getAuthorBooks(id);
    }

    public List<BookAuthors> getBookInstanceInHall(long id) {
        return bookDao.getBookInstanceInHall(id);
    }

    public void bookToStock(long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        bookDao.insertWithState(id, 1, user.getPK());
    }

    @Transactional
    public void orderBook(DoubleIdDto dto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getPK() != dto.getUserId()) {
            throw new BadRequestException();
        }
        List<BookAuthors> books = bookDao.getBookInstanceInHall(dto.getId());
        if (books.isEmpty()) {
            throw new BadRequestException("This book out of stock");
        }
        BookAuthors book = books.get(random.nextInt(books.size()));
        bookDao.insertWithState(book.getInstanceId(), 4, user.getPK());
    }

    public List<BookAuthors> getAllOrders() {
        return bookDao.getAllOrders();
    }

    public void approveOrder(DoubleIdDto id) {
        bookDao.insertWithState(id.getId(), 2, id.getUserId());
    }

    public List<AuthorDto> getAuthors() {
        return bookDao.getAuthors();
    }

    public List<AuthorDto> getGenres() {
        return bookDao.getGenres();
    }

    public void create(CreateBookDto dto) {
        try {
            Long[] authors = Arrays.stream(dto.getAuthors()).mapToLong(AuthorDto::getId).boxed().toList().toArray(new Long[0]);
            bookDao.createBook(dto.getName(), dto.getGenre().getId(), authors);
        } catch (SQLException e) {
            log.error("Error", e);
            throw new BadRequestException();
        }
    }

    public List<ReviewDto> getReviews(long id) {
        return bookDao.getReviews(id);
    }

    public void addReview(ReviewDto reviewDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        bookDao.createReview(user.getPK(), reviewDto.getText(), reviewDto.getBookId());
    }

    public List<AuthorDto> getStates() {
        return bookDao.getStates();
    }

    public void addInstance(BookAuthors bookDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            bookDao.addInstance(bookDto.getId(), bookDto.getInstanceId(), user.getPK());
        } catch (SQLException e) {
            throw new BadRequestException();
        }
    }

    public List<BookAuthors> getUsersOnEvent(long id) {
        return bookDao.getUsersOnEvent(id);
    }

    public List<BookAuthors> getHall() {
        return bookDao.getHall();
    }
}
