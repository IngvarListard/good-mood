-- :name create-user! :<! :1
-- :doc creates a new user record
INSERT INTO users
(first_name, last_name, email, pass)
VALUES (:first-name, :last-name, :email, :pass)
returning id;

-- :name update-user! :! :n
-- :doc updates an existing user record
UPDATE users
SET first_name = :first-name, last_name = :last-name, email = :email
WHERE id = :id

-- :name get-user :? :1
-- :doc retrieves a user record given the id
SELECT * FROM users
WHERE id = :id

-- :name delete-user! :! :n
-- :doc deletes a user record given the id
DELETE FROM users
WHERE id = :id

-- :name get-users :? :*
-- :doc retrieves a user record given the id
SELECT * FROM users limit 10
