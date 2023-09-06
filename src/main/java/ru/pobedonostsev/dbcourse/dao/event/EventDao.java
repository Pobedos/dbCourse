package ru.pobedonostsev.dbcourse.dao.event;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import ru.pobedonostsev.dbcourse.dto.AuthorDto;
import ru.pobedonostsev.dbcourse.dto.CreateEventDto;
import ru.pobedonostsev.dbcourse.dto.EventDto;
import ru.pobedonostsev.dbcourse.model.Event;
import ru.pobedonostsev.framework.dao.BaseDao;
import ru.pobedonostsev.framework.exception.DaoException;

@Repository
public class EventDao extends BaseDao<Long, Event> {
    private static final String SQL_INSERT =
        """
            insert into event (date, host, name) values (:date, :host, :name)
        """;
    public static final RowMapper<EventDto> EVENT_DTO_ROW_MAPPER = (rs, rowNum) -> new EventDto()
        .setId(rs.getLong("id"))
        .setDate(rs.getString("date"))
        .setHost(rs.getLong("host"))
        .setName(rs.getString("name"))
        .setHostName(rs.getString("login"));

    @Override
    public void store(Event entity) throws DaoException {
        MapSqlParameterSource map = new MapSqlParameterSource("date", entity.getDate())
            .addValue("host", entity.getHost())
            .addValue("name", entity.getName());
        client.update(SQL_INSERT, map);
    }

    @Override
    public void update(Event entity) throws DaoException {

    }

    @Override
    public String getTableName() {
        return "Event";
    }

    @Override
    public String getPKColumn() {
        return "id";
    }

    @Override
    public RowMapper<Event> getMapper() {
        return (rs, rowNum) -> new Event().setId(rs.getLong("id"))
            .setDate(rs.getString("date"))
            .setHost(rs.getLong("host"))
            .setName(rs.getString("name"));
    }

    private static final String SQL_SELECT =
        """
            select event.*, u.login from event
            join users u on u.id = event.host
            order by date desc;
        """;

    public List<EventDto> getAll() {
        return client.query(SQL_SELECT, EVENT_DTO_ROW_MAPPER);
    }

    private static final String SQL_SELECT_BY_ID =
        """
            select event.*, u.login from event
            join users u on u.id = event.host
            where event.id = :id;
        """;

    public EventDto getEvent(long id) {
        List<EventDto> events = client.query(SQL_SELECT_BY_ID, new MapSqlParameterSource("id", id), EVENT_DTO_ROW_MAPPER);
        return events.isEmpty() ? null : events.get(0);
    }

    private static final String SQL_JOIN =
        """
            insert into participants (event_id, user_id)
            values (:eventId, :userId)
        """;

    private static final String SQL_LEAVE =
        """
            delete from participants
            where user_id = :userId and event_id = :eventId;
        """;

    public void join(long id, long userId) {
        MapSqlParameterSource map = new MapSqlParameterSource("userId", userId)
            .addValue("eventId", id);
        client.update(SQL_JOIN, map);
    }

    public void leave(long id, long userId) {
        MapSqlParameterSource map = new MapSqlParameterSource("userId", userId)
            .addValue("eventId", id);
        client.update(SQL_LEAVE, map);
    }

    private static final String SQL_DELETE =
        """
            delete from event_book
            where event_id = :eventId and book_id = :bookId;
        """;

    public void deleteBook(long id, long bookId) {
        MapSqlParameterSource map = new MapSqlParameterSource("bookId", bookId)
            .addValue("eventId", id);
        client.update(SQL_DELETE, map);
    }

    private static final String SQL_ADD =
        """
            insert into event_book (event_id, book_id)
            values (:eventId, :book_id)
        """;

    public void addBook(long book, long event) {
        MapSqlParameterSource map = new MapSqlParameterSource("bookId", book)
            .addValue("eventId", event);
        client.update(SQL_ADD, map);
    }

    public void addEvent(CreateEventDto dto) throws SQLException {
        Connection connection = client.getJdbcTemplate().getDataSource().getConnection();
        CallableStatement callableStatement = connection.prepareCall("CALL createevent(?, ?, ?, ?)");
        callableStatement.setString(1, dto.getName());
        callableStatement.setLong(2, dto.getHost());
        callableStatement.setArray(3, connection.createArrayOf("bigint", dto.getBooks()));
        callableStatement.setString(4, dto.getTime());
        callableStatement.execute();
        callableStatement.close();
    }
}
