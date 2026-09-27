# 📘 SQL: Памятка по ключевым словам

> Для повседневной работы хватит **~100 слов**. Здесь — они все, с примерами под PostgreSQL.

---

## 🗂 Содержание

- [1. DDL — создание и изменение структуры](#1-ddl)
- [2. DML — работа с данными](#2-dml)
- [3. SELECT — чтение данных](#3-select)
- [4. JOIN — соединение таблиц](#4-join)
- [5. Ограничения (CONSTRAINTS)](#5-ограничения-constraints)
- [6. Типы данных](#6-типы-данных)
- [7. Операторы](#7-операторы)
- [8. Агрегатные функции](#8-агрегатные-функции)
- [9. Транзакции](#9-транзакции)
- [10. Продвинутое: CTE, оконные функции](#10-продвинутое-cte-оконные-функции)
- [11. PostgreSQL-специфика](#11-postgresql-специфика)
- [12. Шпаргалка «одной строкой»](#12-шпаргалка-одной-строкой)

---

## 1. DDL

**Data Definition Language** — создание и изменение структуры БД.

| Слово | Значение | Пример |
|---|---|---|
| `CREATE` | Создать объект | `CREATE TABLE users (...)` |
| `ALTER` | Изменить структуру | `ALTER TABLE users ADD COLUMN age INT` |
| `DROP` | Удалить объект | `DROP TABLE users` |
| `TRUNCATE` | Очистить таблицу (быстро) | `TRUNCATE users` |
| `RENAME` | Переименовать | `ALTER TABLE users RENAME TO clients` |
| `TABLE` | Объект «таблица» | `CREATE TABLE ...` |
| `INDEX` | Индекс для ускорения поиска | `CREATE INDEX idx_email ON users(email)` |
| `VIEW` | Виртуальная таблица | `CREATE VIEW active_users AS ...` |
| `SEQUENCE` | Генератор чисел | `CREATE SEQUENCE user_id_seq` |
| `SCHEMA` | Пространство имён | `CREATE SCHEMA billing` |
| `DATABASE` | База данных | `CREATE DATABASE cordo` |
| `CONSTRAINT` | Именованное ограничение | `ADD CONSTRAINT fk_user FOREIGN KEY ...` |
| `IF EXISTS` / `IF NOT EXISTS` | Условное выполнение | `DROP TABLE IF EXISTS users` |

**Пример:**

```sql
CREATE TABLE users (
    id    UUID         PRIMARY KEY,
    email VARCHAR(320) NOT NULL UNIQUE
);
```

---

## 2. DML

**Data Manipulation Language** — работа с данными.

| Слово | Значение | Пример |
|---|---|---|
| `INSERT` | Вставить строку | `INSERT INTO users (email) VALUES ('a@b.c')` |
| `UPDATE` | Обновить | `UPDATE users SET balance = 100 WHERE id = ...` |
| `DELETE` | Удалить строки | `DELETE FROM users WHERE id = ...` |
| `SELECT` | Выбрать | `SELECT * FROM users` |
| `VALUES` | Значения для вставки | `VALUES (1, 'a'), (2, 'b')` |
| `SET` | Что менять в UPDATE | `SET balance = 100` |
| `FROM` | Источник данных | `SELECT * FROM users` |
| `RETURNING` | Вернуть вставленное (PG) | `INSERT ... RETURNING id` |

**Пример:**

```sql
INSERT INTO users (id, email, password)
VALUES (gen_random_uuid(), 'a@b.c', 'hash')
RETURNING id;
```

---

## 3. SELECT

Чтение данных. **Порядок слов в запросе фиксирован.**

```sql
SELECT   email, COUNT(*) AS cnt       -- 1. что выбрать
FROM     users u                       -- 2. откуда
JOIN     user_subscription s ON ...    -- 3. присоединить
WHERE    u.balance > 0                 -- 4. фильтр строк
GROUP BY email                         -- 5. группировка
HAVING   COUNT(*) > 1                  -- 6. фильтр групп
ORDER BY cnt DESC                      -- 7. сортировка
LIMIT    10 OFFSET 20;                 -- 8. пагинация
```

| Слово | Значение |
|---|---|
| `SELECT` | Что вернуть |
| `DISTINCT` | Только уникальные строки |
| `AS` | Псевдоним (`email AS user_email`) |
| `FROM` | Источник |
| `WHERE` | Фильтр **строк** |
| `GROUP BY` | Сгруппировать |
| `HAVING` | Фильтр **групп** |
| `ORDER BY` | Сортировка |
| `ASC` / `DESC` | По возрастанию / убыванию |
| `LIMIT` / `OFFSET` | Сколько строк / с какой начать |

---

## 4. JOIN

Соединение таблиц.

| Тип | Что делает |
|---|---|
| `INNER JOIN` | Только совпадающие строки |
| `LEFT JOIN` | Все строки слева + совпадения справа |
| `RIGHT JOIN` | Все строки справа + совпадения слева |
| `FULL JOIN` | Все строки с обеих сторон |
| `CROSS JOIN` | Декартово произведение |
| `ON` | Условие соединения |
| `USING (col)` | Сокращение для одинаковых колонок |

**Пример:**

```sql
SELECT u.email, s.plan_name
FROM users u
LEFT JOIN user_subscription s ON s.user_id = u.id;
```

---

## 5. Ограничения (CONSTRAINTS)

Ограничения на колонки и таблицы.

| Слово | Смысл | Пример |
|---|---|---|
| `PRIMARY KEY` | Уникальный ID, NOT NULL (включает оба) | `id UUID PRIMARY KEY` |
| `FOREIGN KEY` / `REFERENCES` | Ссылка на другую таблицу | `user_id UUID REFERENCES users(id)` |
| `UNIQUE` | Значения не повторяются | `email VARCHAR(320) UNIQUE` |
| `NOT NULL` | Обязательное поле | `email VARCHAR(320) NOT NULL` |
| `CHECK` | Логическое условие | `CHECK (balance >= 0)` |
| `DEFAULT` | Значение по умолчанию | `balance BIGINT DEFAULT 0` |
| `ON DELETE CASCADE` | Удалить связанные строки | `REFERENCES users(id) ON DELETE CASCADE` |
| `ON DELETE SET NULL` | Обнулить ссылку | `... ON DELETE SET NULL` |

> ⚠️ `PRIMARY KEY` **уже включает** `NOT NULL` + `UNIQUE` — дублировать не нужно.
>
> ⚠️ `UNIQUE` **пропускает несколько NULL** — если NULL запрещён, добавь `NOT NULL`.

---

## 6. Типы данных

| Тип | Что хранит |
|---|---|
| `INT` / `INTEGER` | Целое 4 байта |
| `BIGINT` | Целое 8 байт |
| `SMALLINT` | Целое 2 байта |
| `SERIAL` / `BIGSERIAL` | Автоинкремент |
| `NUMERIC(p, s)` / `DECIMAL` | Точное число с дробью (**для денег**) |
| `REAL` / `DOUBLE PRECISION` | Число с плавающей точкой |
| `VARCHAR(n)` | Строка переменной длины до n |
| `TEXT` | Строка без ограничения |
| `BOOLEAN` | `TRUE` / `FALSE` |
| `DATE` | Дата |
| `TIME` | Время |
| `TIMESTAMP` | Дата+время без TZ |
| `TIMESTAMPTZ` | Дата+время с часовым поясом |
| `INTERVAL` | Промежуток времени |
| `UUID` | Универсальный ID (16 байт) |
| `JSON` / `JSONB` | JSON-документ (`JSONB` быстрее) |
| `BYTEA` | Бинарные данные |

---

## 7. Операторы

| Оператор | Значение |
|---|---|
| `=` `<>` `!=` | Равно / не равно |
| `<` `>` `<=` `>=` | Сравнения |
| `AND` `OR` `NOT` | Логика |
| `BETWEEN a AND b` | В диапазоне |
| `IN (1,2,3)` | В списке |
| `LIKE` / `ILIKE` | Шаблон (`%` — любые, `_` — один) |
| `IS NULL` / `IS NOT NULL` | Проверка на NULL |
| `EXISTS` | Существует ли подзапрос |
| `\|\|` | Конкатенация строк |
| `+ - * / %` | Арифметика |

> ⚠️ `NULL = NULL` → `NULL`, а не `TRUE`. Только `IS NULL`.

---

## 8. Агрегатные функции

| Функция | Что делает |
|---|---|
| `COUNT(*)` | Сколько строк |
| `COUNT(DISTINCT col)` | Сколько уникальных |
| `SUM(col)` | Сумма |
| `AVG(col)` | Среднее |
| `MIN(col)` / `MAX(col)` | Минимум / максимум |
| `STRING_AGG(col, ',')` | Склеить строки |
| `ARRAY_AGG(col)` | Собрать в массив |

**Пример:**

```sql
SELECT plan_name, COUNT(*)
FROM user_subscription
GROUP BY plan_name;
```

---

## 9. Транзакции

| Слово | Значение |
|---|---|
| `BEGIN` / `START TRANSACTION` | Начать транзакцию |
| `COMMIT` | Зафиксировать |
| `ROLLBACK` | Откатить |
| `SAVEPOINT name` | Точка отката |

**Пример:**

```sql
BEGIN;
UPDATE users SET balance = balance - 100 WHERE id = '...';
UPDATE users SET balance = balance + 100 WHERE id = '...';
COMMIT;
```

---

## 10. Продвинутое: CTE, оконные функции

### CTE — обобщённое табличное выражение

```sql
WITH rich_users AS (
    SELECT * FROM users WHERE balance > 10000
)
SELECT * FROM rich_users;
```

| Слово | Значение |
|---|---|
| `WITH name AS (...)` | Объявить CTE |
| `UNION` | Объединить (без дублей) |
| `UNION ALL` | Объединить (с дублями, быстрее) |
| `INTERSECT` | Пересечение |
| `EXCEPT` | Разность |

### Оконные функции

```sql
SELECT email,
       ROW_NUMBER() OVER (ORDER BY balance DESC) AS rank
FROM users;
```

| Слово | Значение |
|---|---|
| `OVER (...)` | Признак оконной функции |
| `PARTITION BY` | Разбить на группы |
| `ROW_NUMBER()` | Номер строки |
| `RANK()` / `DENSE_RANK()` | Ранг с пропусками / без |
| `LAG(col)` / `LEAD(col)` | Значение из предыдущей / следующей |

---

## 11. PostgreSQL-специфика

| Слово | Значение |
|---|---|
| `RETURNING` | Вернуть изменённые строки |
| `ON CONFLICT ... DO UPDATE` | UPSERT |
| `ON CONFLICT DO NOTHING` | Игнорировать конфликт |
| `::тип` | Приведение: `'123'::INT` |
| `gen_random_uuid()` | Генерация UUID |
| `NOW()` / `CURRENT_TIMESTAMP` | Текущее время |
| `COALESCE(a, b)` | Первое не-NULL |
| `NULLIF(a, b)` | NULL, если a = b |
| `CASE WHEN ... THEN ... ELSE ... END` | Условие |
| `EXPLAIN` / `EXPLAIN ANALYZE` | План запроса |
| `VACUUM` / `ANALYZE` | Обслуживание БД |
| `ILIKE` | LIKE без учёта регистра |
| `~` / `~*` | Регулярка (с/без учёта регистра) |

**UPSERT-пример:**

```sql
INSERT INTO users (id, email)
VALUES (gen_random_uuid(), 'a@b.c')
ON CONFLICT (email) DO NOTHING;
```

---

## 12. Шпаргалка «одной строкой»

### DDL

```
CREATE TABLE  ALTER TABLE  DROP  TRUNCATE  INDEX
PRIMARY KEY  FOREIGN KEY  UNIQUE  NOT NULL  DEFAULT
REFERENCES  CHECK  CASCADE
```

### DML

```
INSERT  UPDATE  DELETE  SELECT  VALUES  SET  RETURNING
```

### SELECT

```
FROM  WHERE  JOIN  ON  GROUP BY  HAVING
ORDER BY  ASC  DESC  LIMIT  OFFSET  DISTINCT  AS
```

### Логика

```
AND  OR  NOT  IN  BETWEEN  LIKE  ILIKE
IS NULL  EXISTS  CASE
```

### Агрегаты

```
COUNT  SUM  AVG  MIN  MAX
```

### Транзакции

```
BEGIN  COMMIT  ROLLBACK  SAVEPOINT
```

---

## 🎯 Правила, которые стоит помнить

1. **Порядок слов в SELECT фиксирован** — `SELECT → FROM → WHERE → GROUP BY → HAVING → ORDER BY → LIMIT`. Нарушать нельзя.
2. **`WHERE` фильтрует строки, `HAVING` — группы.** `HAVING` без `GROUP BY` — почти всегда ошибка.
3. **`NULL` — не значение.** `NULL = NULL` → `NULL`. Только `IS NULL`.
4. **`PRIMARY KEY` = `NOT NULL` + `UNIQUE`.** Дублировать не нужно.
5. **`UNIQUE` разрешает несколько NULL.** Нужен `NOT NULL` — добавь явно.
6. **`VARCHAR(320)` для email** — практичный максимум (реально 254 по RFC 5321).
7. **Деньги храни в `NUMERIC`, не в `FLOAT`.** Иначе копейки «поплывут».
8. **Для ID используй `UUID`** — быстрее и безопаснее, чем `VARCHAR`.
9. **В Flyway-миграциях `IF NOT EXISTS` не нужен** — Flyway сам следит за применением.
10. **Пароль никогда не храни сырым.** Только хеш (bcrypt, Argon2).

---

## 📚 Где смотреть дальше

- [PostgreSQL Documentation (RU)](https://postgrespro.ru/docs/postgresql/16/sql)
- [Список ключевых слов PostgreSQL](https://postgrespro.ru/docs/postgresql/16/sql-keywords-appendix)
- [PostgreSQL Tutorial (EN)](https://www.postgresqltutorial.com/)

---

> 💡 **Совет:** распечатай «Шпаргалку» (раздел 12) и держи рядом. Остальное — читай по мере необходимости. Заучивать все 800 слов PostgreSQL бессмысленно — реально нужно ~100.