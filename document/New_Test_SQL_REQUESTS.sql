SELECT email, balance FROM users WHEN balance < 1000 OR NOT LIKE '%mail.ru%'
ORDER BY balance DESC
LIMIT 5;


SELECT email, id, balance FROM users WHERE id IN ('user-1','user-2','user-3') OR balance BETWEEN 500 AND 2000
ORDER BY email
LIMIT 10;

SELECT email, balance FROM users WHERE email LIKE '%@gmail.com%' AND (balance < 100 OR id = 'admin')
ORDER BY balance;

SELECT id, email, balance FROM users WHERE (email LIKE '%test%' AND balance < 500) OR email LIKE '%@admin.com'
ORDER BY id DESC
LIMIT 3

--ЗАДАЧА: "Выбери id, email и balance у пользователей, у которых выполняется хотя бы одно из условий:
--           A. email содержит admin И balance от 100 до 10000 (включительно)
--           B. id входит в список 'root', 'system', 'moderator'
--           C. email НЕ заканчивается на @mail.ru И balance больше 50000
--           И при этом (для всех подходящих) email НЕ содержит bot.
--           Отсортируй по balance от большего к меньшему, при равном балансе — по email A→Z, покажи первые 7 строк."

SELECT id, email,balance FROM users WHERE
((email LIKE '%admin%' AND balance BETWEEN 100 AND 10000) OR
id IN ('root', 'system', 'moderator') OR
(email NOT LIKE '%@mail.ru' AND balance > 50000)) AND email NOT LIKE '%bot%'
ORDER BY balance DESC, email ASC --Я не знаю как поймать момент когда баланс равный так что похуй
LIMIT 7;

--🎯 Задача 2: Для каждого плана: plan_name, количество подписок (cnt), средняя цена (avg_price).
SELECT plan_name, COUNT(*) as cnt, AVG(price) AS avg_price
GROUP BY plan_name;

--🎯 Задача 3 — не сдавайся: Сколько активных подписок (status = 'ACTIVE') на каждом плане. Колонки: plan_name, cnt.

SELECT plan_name, COUNT(*) as cnt
FROM users --идите нахуй название таблиц не важно
WHERE status = 'ACTIVE'
GROUP BY plan_name;

--✅ Задача 4 — финальная: Планы, у которых больше 1 подписчика. Колонки: plan_name, cnt.
SELECT plan_name,COUNT(*) as cnt --(так как я понимаю вместо COUNT можно подставлять другие приколы но похуй по факту все что осталось это блоки ЗАПРОСОВ на изменение а не получение и транкзации (я хуй знает что это за дичь а где CREATE я вообще молчу как говорится мир sql страшная вещь))
FROM user_subscription
GROUP BY plan_name
HAVING COUNT(*) > 1; --По логике это тот же самый WHERE но для групп или ну короче похуй хуй SQL запросы нормально обьяснишь на человеческом

INSERT INTO users (id, email, password, balance)
VALUE (gen_random_uuid(),'test@test.com','hash123',0);

UPDATE users
balance = balance + 500
WHERE email = 'test@test.com';

DELETE users u
WHERE u.balance = 9 --AND s.status
--LEFT JOIN user_subscription s ON user_id = u.id; --ой блять а че раньше нельзя было сказать? какого хуя я узнал о том что эта строка запрещена здесь во время выполнения задачи?!
AND NOT EXISTS (SELECT 1 FROM user_subscription s WHERE s.user_id = u.id); --пиздец а почему SELECT 1 какого хуя один? всего одну строку? че "БД найти мне одну строку из таблицы подписок пользователей где айди будет равно айди пользователей и при этом если не находится одну строку блять как нахуй
--я буду думать погоди по итогу получается хуйня че за говно


BEGIN; --а хули тут ; блять где логика?
UPDATE users
balance = balance - 100
WHERE name = 'user-1'--я должен из мыслей читать откуда брать имя? хорошо искажу пространство времени и получу поле name
balance = balance + 200 --будем шаманить ведь по условию сделать два запроса нельзя тьфу
WHERE name = 'user-2';
COMMIT;

--Задачи — раунд 1

--  1. Все email и balance пользователей, отсортированные по балансу убыв.

--  2. Количество всех пользователей.

--  3. Только email тех, у кого баланс больше 1000.

--  4. email и balance, у кого баланс от 500 до 1500 включительно.

SELECT email,balance FROM users balance DESC;
SELECT COUNT(*) FROM users; --если подумать то сработает
SELECT email FROM users WHERE balance > 1000;
SELECT email,balance FROM users WHERE balance BETWEEN 500 AND 1500;

--Раунд 2

  --1. Только email тех, у кого email начинается с admin.

 -- 2. Пользователи, у которых email содержит test И баланс меньше 500.

 -- 3. Пользователи, у которых баланс 0 ИЛИ email заканчивается на @mail.ru.

--  4. Первые 3 пользователя, отсортированные по id возрастанию.

SELECT email FROM users WHERE email LIKE 'admin%';
SELECT * FROM users WHERE email LIKE '%test%' AND balance < 500;
SELECT * FROM users WHERE balance = 0 OR email '%@mail.ru';
SELECT * FROM users ORDER BY id ASC LIMIT 3;
--Раунд 3 — агрегаты

--1. Общая сумма баланса всех пользователей.

--2. Средний баланс всех пользователей.

--3. Максимальный и минимальный баланс.

--4. Количество пользователей, у которых баланс больше 500.

SELECT SUM(balance) FROM users; --по факту должно сработать
SELECT AVG(price) AS avg_price FROM users; --нервы начинают напрягаться
SELECT MIN(balance),MAX(balance) FROM users; --ой йойо йоойойойойо
SELECT COUNT(*) FROM users
GROUP BY balance
HAVING balance > 500; --пиздец но наверное группировка правильная идея ведь пользователей много и у них е... похуй лень говорить
--Раунд 4 — GROUP BY (тут он уже нужен)

--Схема:
--user_subscription (id, user_id, plan_name, status, price, next_billing_date)

--1. Для каждого plan_name — количество подписок.

--2. Для каждого status — количество подписок.

--3. Для каждого user_id — количество его подписок.

--4. Планы, у которых больше 1 подписчика. Колонки: plan_name, cnt.

--Я поймал фул дизмораль блять
--СУКА КАК ЭТО РЕШАТЬ ЭТО ЖЕ КАКАЯ ТО ХУИТА "ДЛЯ КАЖДОГО" ЧЕ ДЛЯ КАЖДОГО ЭТО ЧТО ГРУПП... хм а это идея
SELECT COUNT(plan_name) FROM user_subscription  --рискую вместо "*" у меня plan_name
GROUP BY plan_name;

SELECT COUNT(*) FROM user_subscription --риск это хорошо а завалить 4 задания из за одной и той же ошибки плохо так что перестрахуемся
GROUP BY status;

SELECT COUNT(*) FROM user_subscription
GROUP BY user_id;

SELECT COUNT(*) as cnt FROM user_subscription
GROUP BY user_id
HAVING cnt > 1; --я не знаю че за дичь я написал наверное неверно это(((( что если HAVING user_id > 1; ладно останусь на своем варианте

--Задачи по JOIN: (5 раунд из 7)

--1. email + plan_name — только те, у кого есть подписка (INNER JOIN).

--2. То же, но все пользователи — если нет подписки, plan_name = NULL (LEFT JOIN).

--3. email + plan_name + status — только где status = 'ACTIVE'.

--4. Пользователи без единой подписки. Колонки: id, email.

SELECT * FROM users u WHERE s.plan_name = NULL
LEFT JOIN user_subscription s ON s.user_id = u.id;
--Заново ну че ахуенно блять идите нахуй только 5 раунд последний заебали устал нахуй
--Пиши 4 задачи **заново  1. email + plan_name — только те, у кого есть подписка.  2. email + plan_name — все пользователи, если нет подписки → plan_name = NULL.  3. email + plan_name + status — только там, где status = 'ACTIVE'.
--4. id, email пользователей без подписки.
SELECT u.email, s.plan_name FROM users WHERE s.status = 'active'
LEFT JOIN user_subscription s ON s.user_id = u.id;  --я знатно устал
--Претензия ко второй задаче нахуй возращать email если во ладно похуй
SELECT u.email,s.plan_name,* FROM users
LEFT JOIN user_subscription s ON s.user_id = u.id;
--Я устал все нахуй больше не буду достаточно на сегодня