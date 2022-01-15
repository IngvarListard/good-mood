-- :name get-reports :? :*
SELECT * FROM reports;

-- :name get-report-by-id :? :1
SELECT * FROM reports WHERE id = :id;

-- :name create-report! :<! :1
INSERT INTO reports (user_id, comment, mood_grade, activity_grade, happiness_grade, details, report_date)
VALUES (:user-id, :comment, :mood-grade, :activity-grade, :happiness-grade, :details::json, :report-date::timestamp)
RETURNING id;

-- :name update-report-by-id! :! :1
UPDATE reports
SET user_id = :user-id, comment = :comment, mood_grade = :mood-grade, activity_grade = :activity-grade, happiness_grade = :happiness-grade, details = :details::json, report_date = :report-date::timestamp
WHERE id = :id;

-- :name delete-report-by-id! :! :1
DELETE FROM reports WHERE id = :id;
