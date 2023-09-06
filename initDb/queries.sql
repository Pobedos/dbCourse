-- 1 Вывести все книги и авторов
select q.id, q.name as name, g.name as genre, q.authors, q.count
from (select book.id, genre_id, book.name, count(*) as count, string_agg(a.name, ', ') as authors
      from book
               join book_author ba on book.id = ba.book_id
               join author a on ba.author_id = a.id
      group by book.id) as q
         join genre g on q.genre_id = g.id;

-- 2 Книги которые находятся на руках
select l.book_id, s.name, u.firstname || ' ' || u.lastname as username
from (select log.id, book_id, timestamp, row_number() over (partition by book_id order by timestamp desc) as row_number
      from log) as q
         join log l on q.id = l.id
         join status s on l.status_id = s.id
         join users u on l.client_id = u.id
where row_number = 1
  and s.name = 'IN_HALLE';

-- 3 Самые популярные книги
select q.id, q.name as name, g.name as genre, coalesce(q2.count, 0) as count, q3.authors_id, q3.authors
from (select book.id, genre_id, book.name
      from book
               left join bookinstance bi on book.id = bi.book_id
               left join log l on bi.id = l.book_id
      where book.id = 123
      group by book.id) as q
         join genre g on q.genre_id = g.id
         left join (select book.id, count(*) as count
                    from book
                             left join bookinstance bi on book.id = bi.book_id
                             left join log l2 on bi.id = l2.book_id
                             left join status s on l2.status_id = s.id
                    where book.id = 123
                      and s.name = 'IN_HAND'
                    group by book.id) as q2 on q2.id = q.id
         join (select book.id, string_agg(a.id::text, ', ') as authors_id, string_agg(a.name, ', ') as authors
               from book
                        join book_author ba on book.id = ba.book_id
                        join author a on ba.author_id = a.id
               where book.id = 123
               group by book.id) as q3 on q.id = q3.id;

-- 4 Самые активные читатели
select u.firstname || ' ' || u.lastname as client, count(u.id) as count
from log
         join status s on log.status_id = s.id
         join users u on log.client_id = u.id
where s.name = 'IN_HAND'
group by u.id
order by count desc;

-- 5 Все книги которые необходимо переложить на склад, так как плохое состояние
select bi.id, b.name, bs.state
from log
         join bookinstance bi on bi.id = log.book_id
         join bookstate bs on bs.id = bi.state_id
         join status s on log.status_id = s.id
         join book b on b.id = bi.book_id
where bs.state = 'POOR'
  and s.name = 'IN_HALL';

-- 6 Юзеры которые прочитали более 5 книг за последнюю неделю
select u.id, count(*) as count
from log
         join status s on log.status_id = s.id
         join users u on u.id = log.client_id
where log.timestamp between now() - interval '1 week' and now()
  and s.name = 'IN_HAND'
group by u.id
having count(*) > 5;

-- 7 Вывести юзеров которые не брали книги
select users.id, login
from users
         join role r on r.id = users.role
where not exists(
        select client_id
        from log
        where client_id = users.id)
  and r.name = 'ROLE_CLIENT';

-- 8 Вывести всех авторов у которых есть книги в жанре и Fantasy и Mystery
SELECT a.name
FROM Author a
         JOIN book_author ba ON a.id = ba.author_id
         JOIN Book b ON ba.book_id = b.id
         JOIN Genre g ON b.genre_id = g.id
WHERE g.name = 'Fantasy'
INTERSECT
SELECT a.name
FROM Author a
         JOIN book_author ba ON a.id = ba.author_id
         JOIN Book b ON ba.book_id = b.id
         JOIN Genre g ON b.genre_id = g.id
WHERE g.name = 'Mystery';

-- 9 Вывести всех авторов и их книги
select author.name, string_agg('''' || b.name || '''', ', ') as books
from author
         join book_author ba on author.id = ba.author_id
         join book b on ba.book_id = b.id
group by author.id;

-- 10 Топ 5 самых популярных книг
select qq.id, name, count, authors
from (select q.id, avg(q.count) as count, string_agg(a.name, ', ') as authors
      from (select b.id, count(b.id) as count
            from log
                     join bookinstance bi on bi.id = log.book_id
                     join book b on b.id = bi.book_id
                     join status s on s.id = log.status_id
            where s.name = 'IN_HAND'
            group by b.id) as q
               join book_author ba on q.id = ba.book_id
               join author a on a.id = ba.author_id
      group by q.id) as qq
         join book b on qq.id = b.id
order by count desc
limit 5;

-- select distinct on (bi.id) bi.id, b.name, s.name, bs.state
-- from log
--          join status s on s.id = log.status_id
--          join bookinstance bi on log.book_id = bi.id
--          join bookstate bs on bi.state_id = bs.id
--          join book b on b.id = bi.book_id
--          join book_author ba on b.id = ba.book_id
--          join author a on a.id = ba.author_id
-- where client_id = 312;
--
-- select q.id, q.book_name, q.client_id
-- from (select distinct on (log.book_id) b.id, b.name as book_name, s.name, timestamp, client_id
--       from log
--                join status s on s.id = log.status_id
--                join bookinstance bi on bi.id = log.book_id
--                join book b on b.id = bi.book_id
--       order by log.book_id, timestamp desc) as q
-- where q.name = 'IN_ORDER'
-- order by q.timestamp;
--
-- select q.book_id
-- from (select distinct on (log.book_id) book_id, s.name
--       from log
--                join status s on s.id = log.status_id
--       order by log.book_id, timestamp desc) as q
-- where q.name = 'IN_HAND'
-- order by q.book_id;
--
-- select book_id, g.name as genre, book.name
-- from book
--          join book_author ba on book.id = ba.book_id
--          join author a on a.id = ba.author_id
--          join genre g on book.genre_id = g.id
-- where a.id = 122;
--
-- select q.book_id as id, bs.state, b.name
-- from (select distinct on (log.book_id) book_id, s.name
--       from log
--                join status s on s.id = log.status_id
--       order by log.book_id, timestamp desc) as q
--          join bookinstance bi on bi.id = q.book_id
--          join book b on b.id = bi.book_id
--          left join bookstate bs on bi.state_id = bs.id
-- where b.id = 123
--   and q.name = 'IN_HALL'
-- order by bs.id desc;
--
-- select q.id, q.book_name, q.state, q.client_id, q.user_name, q.book_id
-- from (select distinct on (log.book_id) bi.id,
--                                        b.name                           as book_name,
--                                        s.name,
--                                        bs.state,
--                                        timestamp,
--                                        client_id,
--                                        u.firstname || ' ' || u.lastname as user_name,
--                                        b.id                             as book_id
--       from log
--                join status s on s.id = log.status_id
--                join bookinstance bi on bi.id = log.book_id
--                join book b on b.id = bi.book_id
--                join bookstate bs on bi.state_id = bs.id
--                join users u on log.client_id = u.id
--       order by log.book_id, timestamp desc) as q
-- where q.name = 'IN_ORDER'
-- order by q.timestamp;
--
-- select q.id, q.bi_id, q.book_name
-- from (select distinct on (log.book_id) b.id, bi.id as bi_id, b.name as book_name, s.name, timestamp, client_id
--       from log
--                join status s on s.id = log.status_id
--                join bookinstance bi on bi.id = log.book_id
--                join book b on b.id = bi.book_id
--       order by log.book_id, timestamp desc) as q
-- where q.name = :status
--   and client_id = :client_id
-- order by q.timestamp;
--
--
--
-- select author.id, author.name
-- from author
--          join book_author ba on author.id = ba.author_id
--          join book b on ba.book_id = b.id
-- where b.id = 123;

-- создать новую книгу
create procedure CreateBook(book_name varchar, genreId bigint, authors bigint[])
    LANGUAGE plpgsql
as
$BODY$
DECLARE
    aId bigint;
    bId bigint;
BEGIN
    insert into book(genre_id, name) values (genreId, book_name);
    bId := currval(pg_get_serial_sequence('book', 'id'));
    FOREACH aId IN ARRAY authors
        loop
            insert into book_author(book_id, author_id) values (bId, aId);
        end loop;
end
$BODY$;

create or replace procedure CreateEvent(event_name varchar, hst bigint, books bigint[], t varchar)
    LANGUAGE plpgsql
as
$BODY$
DECLARE
    bId bigint;
    eId bigint;
BEGIN
    insert into event (date, host, name) values (t, hst, event_name);
    eId := currval(pg_get_serial_sequence('event', 'id'));
    FOREACH bId IN ARRAY books
        loop
            insert into event_book(book_id, event_id) values (bId, eId);
            insert into log (client_id, book_id, status_id, timestamp) values (hst, bId, 5, now());
        end loop;
end
$BODY$;

create procedure AddInstance(bookId bigint, stateId bigint, userId bigint)
    LANGUAGE plpgsql
as
$BODY$
DECLARE
    biId bigint;
BEGIN
    insert into bookinstance(book_id, state_id) values (bookId, stateId);
    biId := currval(pg_get_serial_sequence('bookinstance', 'id'));
    insert into log(client_id, book_id, status_id, timestamp) VALUES (userId, biId, 3, now());
end
$BODY$;

-- сменить пароль
create procedure ChangePassword(user_id bigint, pwd varchar)
    language plpgsql
as
$BODY$
begin
    update users set password = pwd where id = user_id;
end
$BODY$;

-- Нанять работягу в библиотеку
create procedure ChangeUserRole(user_id bigint, new_role varchar)
    language plpgsql
as
$BODY$
declare
    role_id bigint;
begin
    select role_id = id from role where name = new_role;
    update users set role = role_id where id = user_id;
end
$BODY$;

call CreateBook('new Book', 1, '{106, 107}');

insert into event (date, host, name)
VALUES (now(), 320, 'Event1');

select *
from users
         join participants p on users.id = p.user_id
where p.event_id = 2;

select b.id, b.name, s.state
from bookinstance
         join event_book eb on bookinstance.id = eb.book_id
         join book b on b.id = bookinstance.book_id
         join bookstate s on bookinstance.state_id = s.id
where event_id = 2
order by b.name;

delete from participants
where user_id = 320 and event_id = 2;

insert into participants (event_id, user_id)
values (2, 320);

delete from event_book
where event_id = 2 and book_id = 123;

create or replace FUNCTION cleanup_delete_event_procedure()
    returns trigger as
$$
    begin
        delete from event_book where event_book.event_id = OLD.id;
        delete from participants where participants.event_id = OLD.id;
        RETURN OLD;
    end;
$$
    LANGUAGE PLPGSQL;

CREATE or replace TRIGGER cleanup_delete_event
    before delete
    ON event
    FOR EACH ROW
EXECUTE PROCEDURE cleanup_delete_event_procedure();
