(ns mood-tracker.db
  (:require [hugsql.core :as hugsql]))

(def config
  {:classname "org.postgresql.Driver"
   :subprotocol "postgresql"
   :subname "//localhost:5432/mood_tracker"
   :user "postgres"
   :password "postgres"})

(hugsql/def-db-fns "reports.sql")

(comment
  (create-reports-and-user-table config)
  (get-reports config)
  (get-users config)
  (get-user-by-id config {:id 1})
  (update-user-by-id config {:id 1
                             :first-name "Shit"
                             :last-name "Shit"
                             :email "shit"
                             :is-active true})
  (insert-user config {:first-name "Don"
                       :last-name "Chung"
                       :email "test@jap.com"
                       :is-active true}))
