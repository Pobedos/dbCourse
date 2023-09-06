-- 1 - Вывести сумму денег в рублях на счетах каждого типа
SELECT
    a.type,
    SUM(a.balance) as total_balance
FROM
    account a
WHERE
        a.status = 'Active'
  AND a.currency = 'Russian ruble'
GROUP BY
    a.type;
-- 2 - Найти количество переводов конкретного клиента другому клиенту
SELECT
    COUNT(*) as num_transaction
FROM
    transaction t
WHERE
        t.sender_account IN (
        SELECT
            a.account_id
        FROM
            account a
        WHERE
                a.client_id = 15
    )
   OR t.receiver_account IN (
    SELECT
        a.account_id
    FROM
        account a
    WHERE
            a.client_id = 4
);
-- 3 - Вывести список клиентов, их менеджеров и результат их заявок за последний год,
-- где результат заявки "Подтвержден" или "В процессе"
SELECT
    c.full_name,
    pm.full_name AS personal_manager,
    a.result
FROM
    client c
        INNER JOIN application a ON a.client = c.client_id
        INNER JOIN personal_manager pm ON pm.personal_manager_id = a.personal_manager
WHERE
        a.application_date >= (CURRENT_DATE - INTERVAL '1 year')
  AND a.result IN ('Confirmed', 'In progress')
ORDER BY
    a.application_date DESC;
-- 4 - Вывести по убыванию все имена и балансы аккаунтов всех клиентов,
-- у кого был хотя бы один активный аккаунт
SELECT
    c.full_name,
    SUM(a.balance) AS total_balance
FROM
    client c
        INNER JOIN account a ON a.client_id = c.client_id
WHERE
        a.status = 'Active'
GROUP BY
    c.full_name
HAVING
        SUM(a.balance) > 0
ORDER BY
    total_balance DESC;
-- 5 - Найти количество счетов в каждом регионе и их средний баланс
SELECT
    r.region_name,
    AVG(a.balance) as avg_balance,
    COUNT(*) as num_account
FROM
    region r
        INNER JOIN city c ON r.region_id = c.region
        INNER JOIN office o ON c.city_id = o.city_id
        INNER JOIN personal_manager pm ON o.office_id = pm.office
        INNER JOIN client cl ON pm.personal_manager_id = cl.personal_manager
        INNER JOIN account a ON cl.client_id = a.client_id
GROUP BY
    r.region_name;
-- 6 - Найти количество всех аккаунтов в каждой валюте в каждом регионе,
-- с балансом больше 10000 и ставкой больше 5%
SELECT
    r.region_name,
    a.type,
    a.currency,
    COUNT(*) as num_account
FROM
    region r
        INNER JOIN city c ON r.region_id = c.region
        INNER JOIN office o ON c.city_id = o.city_id
        INNER JOIN personal_manager pm ON o.office_id = pm.office
        INNER JOIN client cl ON pm.personal_manager_id = cl.personal_manager
        INNER JOIN account a ON cl.client_id = a.client_id
WHERE
        a.balance > 10000
  AND a.interest_rate > 5
GROUP BY
    r.region_name,
    a.type,
    a.currency;
-- 7 - Вывести топ 5 активных клиентов с наибольшим количество денег и
-- количеством их транзакций
SELECT
    c.full_name,
    a.balance,
    a.currency,
    (
        SELECT
            COUNT(*)
        FROM
            transaction t
        WHERE
                t.sender_account IN (
                SELECT
                    a.account_id
                FROM
                    account a
                WHERE
                        a.client_id = c.client_id
            )
           OR t.receiver_account IN (
            SELECT
                a.account_id
            FROM
                account a
            WHERE
                    a.client_id = c.client_id
        )
    ) as num_transaction
FROM
    client c
        INNER JOIN account a ON c.client_id = a.client_id
WHERE
        a.status = 'Active'
ORDER BY
    a.balance DESC
LIMIT
    5;
-- 8 - Найти средний баланс аккаунтов в конкретном регионе, открытых за последние 5 лет
SELECT
    r.region_name,
    AVG(a.balance) as avg_balance,
    COUNT(*) as num_account
FROM
    region r
        INNER JOIN city c ON r.region_id = c.region
        INNER JOIN office o ON c.city_id = o.city_id
        INNER JOIN personal_manager pm ON o.office_id = pm.office
        INNER JOIN client cl ON pm.personal_manager_id = cl.personal_manager
        INNER JOIN account a ON cl.client_id = a.client_id
WHERE
        a.status = 'Active'
  AND a.open_date >= (CURRENT_DATE - INTERVAL '5 years')
GROUP BY
    r.region_name;
-- 9 - Вывести количество и среднюю величину транзакций клиентов за последний год
WITH client_transaction AS (
    SELECT
        t.sender_account,
        t.receiver_account,
        COUNT(*) as num_transaction,
        AVG(t.amount) as avg_amount
    FROM
        transaction t
    WHERE
            t.date >= (CURRENT_DATE - INTERVAL '1 year')
    GROUP BY
        t.sender_account,
        t.receiver_account
)
SELECT
    c.full_name,
    ct.num_transaction,
    ct.avg_amount
FROM
    client_transaction ct
        INNER JOIN account a ON ct.sender_account = a.account_id
        OR ct.receiver_account = a.account_id
        INNER JOIN client c ON a.client_id = c.client_id;
-- 10 - Вывести тип транзакций и их количество, выполненных каждым менеджером
WITH transaction_by_pm AS (
    SELECT
        t.type,
        COUNT(*) as num_transaction,
        pm.full_name
    FROM
        transaction t
            INNER JOIN account a ON a.account_id = t.sender_account
            OR a.account_id = t.receiver_account
            INNER JOIN client c ON c.client_id = a.client_id
            INNER JOIN personal_manager pm ON pm.personal_manager_id = c.personal_manager
    GROUP BY
        t.type,
        pm.full_name
)
SELECT
    t.full_name,
    t.type,
    SUM(t.num_transaction) as total_transaction
FROM
    transaction_by_pm t
GROUP BY
    t.full_name,
    t.type
ORDER BY
    total_transaction DESC;
