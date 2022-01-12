-- :name insert-user :insert :*
INSERT INTO users (first_name, last_name, email, is_active)
VALUES (:first-name, :last-name, :email, :is-active)
RETURNING id;

-- :name get-users :? :*
SELECT * FROM users;

-- :name get-user-by-id :? :*
SELECT * FROM users WHERE id = :id;

-- :name update-user-by-id :! :1
UPDATE users
SET first_name = :first-name, last_name = :last-name, email = :email, is_active = :is-active
WHERE id = :id;

-- :name delete-user :! :1
DELETE FROM users WHERE id = :id;
