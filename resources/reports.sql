-- :name create-reports-and-user-table
-- :command :execute
-- :result :raw
-- :doc create reports and user table
CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  first_name TEXT,
  last_name TEXT,
  email TEXT,
  is_active BOOL,
  addet_at TIMESTAMP NOT NULL DEFAULT current_timestamp
);

CREATE TABLE reports (
  id SERIAL PRIMARY KEY,
  comment TEXT,
  mood_grade INTEGER,
  report_date TIMESTAMP NOT NULL DEFAULT current_timestamp,
  added_at TIMESTAMP NOT NULL DEFAULT current_timestamp,
  user_id INTEGER REFERENCES users(id)
);

-- :name get-reports :? :*
SELECT * FROM reports;

-- :name get-report-by-id :? :*
SELECT * FROM reports WHERE id = :id;

-- :name insert-report :insert :*
INSERT INTO reports (comment, mood_grade, report_date, user_id)
VALUES (:comment, :mood-grade, :report-date, :user-id)
RETURNING id;

-- :name update-report-by-id :! :1
UPDATE reports
SET comment = :comment, mood_grade = :mood-grade, report_date = :report-date, user_id = :user-id
WHERE id = :id;

-- :name delete-report-by-id :! :1
DELETE FROM contacts WHERE id = :id;

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
