SELECT email, balance FROM users WHEN balance < 1000 OR NOT LIKE '%mail.ru%'
ORDER BY balance DESC
LIMIT 5;


SELECT email, id, balance FROM users WHERE id IN ('user-1','user-2','user-3') OR balance BETWEEN 500 AND 2000
ORDER BY email
LIMIT 10;