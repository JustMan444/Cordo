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