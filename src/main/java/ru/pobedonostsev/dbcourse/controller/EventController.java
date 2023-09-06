package ru.pobedonostsev.dbcourse.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.pobedonostsev.dbcourse.dto.CreateEventDto;
import ru.pobedonostsev.dbcourse.dto.DoubleIdDto;
import ru.pobedonostsev.dbcourse.dto.EventDto;
import ru.pobedonostsev.dbcourse.dto.IdDto;
import ru.pobedonostsev.dbcourse.model.Event;
import ru.pobedonostsev.dbcourse.service.EventService;
import ru.pobedonostsev.framework.exception.DaoException;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private static final Log log = LogFactory.getLog(EventController.class);

    @Autowired
    private EventService eventService;

    @GetMapping
    private List<EventDto> getAll() {
        return eventService.getAll();
    }

    @GetMapping("/{id}")
    private EventDto getEvent(@PathVariable long id) {
        return eventService.getEvent(id);
    }

    @PostMapping("/join")
    private void join(@RequestBody DoubleIdDto id) {
        eventService.join(id);
    }

    @PostMapping("/leave")
    private void leave(@RequestBody DoubleIdDto id) {
        eventService.leave(id);
    }

    @PostMapping
    private void create(@RequestBody CreateEventDto dto) {
        eventService.create(dto);
    }

    @PostMapping("/delete")
    private void deleteBook(@RequestBody DoubleIdDto id) {
        eventService.deleteBook(id);
    }

    @DeleteMapping("/{id}")
    private void deleteEvent(@PathVariable long id) throws DaoException {
        eventService.delete(id);
    }
}
