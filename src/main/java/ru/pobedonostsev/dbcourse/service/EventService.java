package ru.pobedonostsev.dbcourse.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.pobedonostsev.dbcourse.controller.EventController;
import ru.pobedonostsev.dbcourse.dao.event.EventDao;
import ru.pobedonostsev.dbcourse.dto.BookAuthors;
import ru.pobedonostsev.dbcourse.dto.CreateEventDto;
import ru.pobedonostsev.dbcourse.dto.DoubleIdDto;
import ru.pobedonostsev.dbcourse.dto.EventDto;
import ru.pobedonostsev.dbcourse.exception.BadRequestException;
import ru.pobedonostsev.dbcourse.model.Event;
import ru.pobedonostsev.framework.dao.BaseDao;
import ru.pobedonostsev.framework.exception.DaoException;
import ru.pobedonostsev.framework.service.BaseService;

@Service
public class EventService extends BaseService<Long, Event> {
    private static final Log log = LogFactory.getLog(EventService.class);

    @Autowired
    private EventDao eventDao;

    @Autowired
    private BookService bookService;

    @Override
    protected BaseDao<Long, Event> getDao() {
        return eventDao;
    }

    public List<EventDto> getAll() {
        return eventDao.getAll();
    }

    public EventDto getEvent(long id) {
        return eventDao.getEvent(id);
    }

    public void join(DoubleIdDto id) {
        eventDao.join(id.getId(), id.getUserId());
    }

    public void leave(DoubleIdDto id) {
        eventDao.leave(id.getId(), id.getUserId());
    }

    public void deleteBook(DoubleIdDto id) {
        eventDao.deleteBook(id.getId(), id.getUserId());
        bookService.returnBook(id.getUserId());
    }

    @Override
    public void delete(Long id) throws DaoException {
        List<BookAuthors> books = bookService.getUsersOnEvent(id);
        for (BookAuthors book : books) {
            bookService.returnBook(book.getInstanceId());
        }
        super.delete(id);
    }

    public void create(CreateEventDto dto) {
        try {
            eventDao.addEvent(dto);
        } catch (SQLException e) {
            log.error("error", e);
            throw new BadRequestException();
        }
    }
}
