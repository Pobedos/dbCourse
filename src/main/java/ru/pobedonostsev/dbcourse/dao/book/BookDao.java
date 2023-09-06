package ru.pobedonostsev.dbcourse.dao.book;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import ru.pobedonostsev.dbcourse.dto.AuthorDto;
import ru.pobedonostsev.dbcourse.dto.BookAuthors;
import ru.pobedonostsev.dbcourse.dto.ReviewDto;
import ru.pobedonostsev.dbcourse.model.Book;
import ru.pobedonostsev.framework.dao.BaseDao;
import ru.pobedonostsev.framework.exception.DaoException;

@Repository
public class BookDao extends BaseDao<Long, Book> implements BookTable {
    private static final Log log = LogFactory.getLog(BookDao.class);

    public static final String C_AUTHORS = "authors";
    public static final RowMapper<BookAuthors> GET_ALL_ROW_MAPPER = (rs, rowNum) -> new BookAuthors()
        .setId(rs.getLong(C_ID))
        .setName(rs.getString(C_NAME))
        .setAuthorsString(rs.getString(C_AUTHORS))
        .setGenre(rs.getString("genre"))
        .setCount(rs.getLong("count"))
        .setAuthors(rs.getString(C_AUTHORS).split(","));

    public static final RowMapper<BookAuthors> GET_DETAIL_ROW_MAPPER = (rs, rowNum) -> new BookAuthors()
        .setId(rs.getLong(C_ID))
        .setName(rs.getString(C_NAME))
        .setAuthorsString(rs.getString(C_AUTHORS))
        .setGenre(rs.getString("genre"))
        .setCount(rs.getLong("count"))
        .setAuthors(rs.getString(C_AUTHORS).split(","))
        .setAuthorsId(rs.getString(C_AUTHORS_ID).split(","));

    public static final RowMapper<BookAuthors> GET_POPULAR_ROW_MAPPER = (rs, rowNum) -> new BookAuthors()
        .setId(rs.getLong(C_ID))
        .setName(rs.getString(C_NAME))
        .setAuthorsString(rs.getString(C_AUTHORS))
        .setCount(rs.getLong("count"));
    private static final String[] DATA_FIELDS = new String[]{C_NAME, C_GENRE_ID};
    private static final String INSERT_SQL = "INSERT INTO " + TABLE_NAME + " (" + String.join(", ", DATA_FIELDS) + ") VALUES (:" + String.join(", :", DATA_FIELDS) + ')';
    private static final String GET_ALL = """
            select q.id, q.name as name, g.name as genre, q.authors, q.count as count
            from (select book.id, genre_id, book.name, count(*) as count, string_agg(a.name, ', ') as authors
                  from book
                           join book_author ba on book.id = ba.book_id
                           join author a on ba.author_id = a.id
                  group by book.id) as q
                     join genre g on q.genre_id = g.id
        """;
    private static final String GET_POPULAR = """
            select qq.id, name, count, authors
            from (select q.id, avg(q.count) as count, string_agg(a.name, ', ') as authors
                  from (select b.id, b.name, count(b.id) as count
                        from log
                                 join bookinstance bi on bi.id = log.book_id
                                 join book b on b.id = bi.book_id
                                 join status s on s.id = log.status_id
                        where s.name = 'IN_HAND'
                        group by b.id
                        limit 10) as q
                           join book_author ba on q.id = ba.book_id
                           join author a on a.id = ba.author_id
                  group by q.id
            ) as qq
            join book b on qq.id=b.id
            order by count desc
        """;
    private static final String SQL_DETAIL = """
            select q.id, q.name as name, g.name as genre, coalesce(q2.count, 0) as count, q3.authors_id, q3.authors
            from (select book.id, genre_id, book.name
                  from book
                           left join bookinstance bi on book.id = bi.book_id
                           left join log l on bi.id = l.book_id
                  where book.id = :id
                  group by book.id) as q
                     join genre g on q.genre_id = g.id
                     left join (select book.id, count(*) as count
                           from book
                                    left join bookinstance bi on book.id = bi.book_id
                                    left join log l2 on bi.id = l2.book_id
                                    left join status s on l2.status_id = s.id
                           where book.id = :id
                             and s.name = 'IN_HAND'
                           group by book.id
                           ) as q2 on q2.id = q.id
                     join (select book.id, string_agg(a.id::text, ', ') as authors_id, string_agg(a.name, ', ') as authors
                           from book
                                    join book_author ba on book.id = ba.book_id
                                    join author a on ba.author_id = a.id
                           where book.id = :id
                           group by book.id) as q3 on q.id = q3.id;
        """;
    private static final String SQL_USER_BOOKS =
        """
                select q.id, q.bi_id, q.book_name
                from (select distinct on (log.book_id) b.id, bi.id as bi_id, b.name as book_name, s.name, timestamp, client_id
                      from log
                               join status s on s.id = log.status_id
                               join bookinstance bi on bi.id = log.book_id
                               join book b on b.id = bi.book_id
                      order by log.book_id, timestamp desc) as q
                where q.name = :status and client_id = :id
                order by q.timestamp
            """;

    @Override
    public void store(Book entity) throws DaoException {
        MapSqlParameterSource params = new MapSqlParameterSource(C_NAME, entity.getName());
        params.addValue(C_GENRE_ID, entity.getGenreId());
        client.update(INSERT_SQL, params);
    }

    @Override
    public void update(Book entity) throws DaoException {
        store(entity);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getPKColumn() {
        return C_ID;
    }

    @Override
    public RowMapper<Book> getMapper() {
        return (rs, rowNum) -> new Book().setPK(rs.getLong(C_ID)).setGenreId(rs.getLong(C_GENRE_ID)).setName(rs.getString(C_NAME));
    }

    public List<BookAuthors> getAll() {
        return client.query(GET_ALL, GET_ALL_ROW_MAPPER);
    }

    public List<BookAuthors> getPopular() {
        return client.query(GET_POPULAR, GET_POPULAR_ROW_MAPPER);
    }

    public BookAuthors getDetail(long id) {
        List<BookAuthors> books = client.query(SQL_DETAIL, new MapSqlParameterSource("id", id), GET_DETAIL_ROW_MAPPER);
        return books.isEmpty() ? null : books.get(0);
    }

    public List<BookAuthors> getUserDto(long id, String status) {
        MapSqlParameterSource map = new MapSqlParameterSource("id", id);
        map.addValue("status", status);
        return client.query(SQL_USER_BOOKS, map, (rs, rowNum) -> new BookAuthors()
            .setId(rs.getLong(C_ID))
            .setName(rs.getString("book_name"))
            .setInstanceId(rs.getLong("bi_id")));
    }

    private static final String SQL_STATE =
        """
            insert into log (client_id, book_id, status_id, timestamp) values (:client_id, :book_id, :status_id, :timestamp)
        """;

    public void insertWithState(Long id, long status, long userId) {
        MapSqlParameterSource map = new MapSqlParameterSource("book_id", id);
        map.addValue("client_id", userId);
        map.addValue("status_id", status);
        map.addValue("timestamp", Timestamp.valueOf(LocalDateTime.now()));
        client.update(SQL_STATE, map);
    }

    private static final String SQL_GET_AUTHOR =
        """
            select book_id, g.name as genre, book.name from book
            join book_author ba on book.id = ba.book_id
            join author a on a.id = ba.author_id
            join genre g on book.genre_id = g.id
            where a.id = :author_id;
        """;

    public List<BookAuthors> getAuthorBooks(long id) {
        MapSqlParameterSource map = new MapSqlParameterSource("author_id", id);
        return client.query(SQL_GET_AUTHOR, map, (rs, rowNum) -> new BookAuthors()
            .setId(rs.getLong("book_id"))
            .setGenre(rs.getString("genre"))
            .setName(rs.getString("name")));
    }

    private static final String SQL_IN_HALL =
        """
        select q.book_id as id, bs.state from (
                select distinct on (log.book_id) book_id, s.name
                from log
                         join status s on s.id = log.status_id
                order by log.book_id, timestamp desc) as q
            join bookinstance bi on bi.id = q.book_id
            join book b on b.id = bi.book_id
            left join bookstate bs on bi.state_id = bs.id
            where b.id = :book_id and q.name = 'IN_HALL'
            order by bs.id desc;
        """;

    public List<BookAuthors> getBookInstanceInHall(long id) {
        MapSqlParameterSource map = new MapSqlParameterSource("book_id", id);
        return client.query(SQL_IN_HALL, map, (rs, rowNum) -> new BookAuthors()
            .setId(id)
            .setInstanceId(rs.getLong("id"))
            .setState(rs.getString("state")));
    }

    private static final String SQL_ALL_ORDERS =
        """
                select q.id, q.book_name, q.state, q.client_id, q.user_name, q.book_id
                from (select distinct on (log.book_id) bi.id, b.name as book_name, s.name, bs.state, timestamp, client_id, u.firstname || ' ' || u.lastname as user_name, b.id as book_id
                      from log
                               join status s on s.id = log.status_id
                               join bookinstance bi on bi.id = log.book_id
                               join book b on b.id = bi.book_id
                               join bookstate bs on bi.state_id = bs.id
                               join users u on log.client_id = u.id
                      order by log.book_id, timestamp desc) as q
                where q.name = 'IN_ORDER'
                order by q.timestamp;
            """;

    public List<BookAuthors> getAllOrders() {
        return client.query(SQL_ALL_ORDERS, (rs, rowNum) -> new BookAuthors()
            .setId(rs.getLong("book_id"))
            .setUserId(rs.getLong("client_id"))
            .setInstanceId(rs.getLong("id"))
            .setName(rs.getString("book_name"))
            .setState(rs.getString("state"))
            .setUserName(rs.getString("user_name")));
    }

    private static final String SQL_GET_AUTHORS =
        """
                select author.id, author.name from author
                order by author.name
           """;

    public List<AuthorDto> getAuthors() {
        return client.query(SQL_GET_AUTHORS, (rs, rowNum) -> new AuthorDto()
            .setId(rs.getLong("id"))
            .setName(rs.getString("name")));
    }

    private static final String SQL_GET_GENRES =
        """
                select genre.id, genre.name from genre
                order by genre.name
           """;

    public List<AuthorDto> getGenres() {
        return client.query(SQL_GET_GENRES, (rs, rowNum) -> new AuthorDto()
            .setId(rs.getLong("id"))
            .setName(rs.getString("name")));
    }

    public void createBook(String name, long genreId, Long[] authors) throws SQLException {
        Connection connection = client.getJdbcTemplate().getDataSource().getConnection();
        CallableStatement callableStatement = connection.prepareCall("CALL createbook(?, ?, ?)");
        callableStatement.setString(1, name);
        callableStatement.setLong(2, genreId);
        callableStatement.setArray(3, connection.createArrayOf("bigint", authors));
        callableStatement.execute();
        callableStatement.close();
    }

    private static final String SQL_REVIEWS =
        """
                select review.*, u.login
                    from review
                             join users u on u.id = review.client
                    where book = :id
            """;

    public List<ReviewDto> getReviews(long id) {
        return client.query(SQL_REVIEWS, new MapSqlParameterSource("id", id), (rs, rowNum) -> new ReviewDto()
            .setId(rs.getLong("id"))
            .setText(rs.getString("text"))
            .setUsername(rs.getString("login"))
            .setUserId(rs.getLong("client")));
    }

    private static final String SQL_ADD_REVIEW =
    """
        insert into review(client, text, book) VALUES (:userId, :text, :bookId)
    """;

    public void createReview(Long userId, String text, long bookId) {
        MapSqlParameterSource map = new MapSqlParameterSource("userId", userId);
        map.addValue("text", text);
        map.addValue("bookId", bookId);
        client.update(SQL_ADD_REVIEW, map);
    }

    private static final String SQL_GET_STATES =
        """
                select id, state as name from bookstate
                order by state
        """;

    public List<AuthorDto> getStates() {
        return client.query(SQL_GET_STATES, (rs, rowNum) -> new AuthorDto()
            .setId(rs.getLong(C_ID))
            .setName(rs.getString(C_NAME)));
    }

    public void addInstance(long id, long statusId, Long userId) throws SQLException {
        Connection connection = client.getJdbcTemplate().getDataSource().getConnection();
        CallableStatement callableStatement = connection.prepareCall("CALL addinstance(?, ?, ?)");
        callableStatement.setLong(1, id);
        callableStatement.setLong(2, statusId);
        callableStatement.setLong(3, userId);
        callableStatement.execute();
        callableStatement.close();
    }

    private static final String SQL_GET_EVENT =
        """
            select b.id, b.name, s.state, bookinstance.id as iId
            from bookinstance
                     join event_book eb on bookinstance.id = eb.book_id
                     join book b on b.id = bookinstance.book_id
                     join bookstate s on bookinstance.state_id = s.id
            where event_id = :id
            order by b.name;
            """;

    public List<BookAuthors> getUsersOnEvent(long id) {
        return client.query(SQL_GET_EVENT, new MapSqlParameterSource("id", id), (rs, rowNum) -> new BookAuthors()
            .setName(rs.getString(C_NAME))
            .setState(rs.getString("state"))
            .setInstanceId(rs.getLong("iId"))
            .setId(rs.getLong(C_ID)));
    }

    private static final String SQL_IN_HALL_ALL =
        """
        select q.book_id as id, b.id as bid, b.name, bs.state from (
                select distinct on (log.book_id) book_id, s.name
                from log
                         join status s on s.id = log.status_id
                order by log.book_id, timestamp desc) as q
            join bookinstance bi on bi.id = q.book_id
            join book b on b.id = bi.book_id
            left join bookstate bs on bi.state_id = bs.id
            where q.name = 'IN_HALL'
            order by bs.id desc;
        """;

    public List<BookAuthors> getHall() {
        return client.query(SQL_IN_HALL_ALL, (rs, rowNum) -> new BookAuthors()
            .setId(rs.getLong("bid"))
            .setInstanceId(rs.getLong("id"))
            .setState(rs.getString("state"))
            .setName(rs.getString("name")));
    }
}
