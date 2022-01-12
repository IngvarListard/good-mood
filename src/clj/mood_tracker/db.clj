(ns mood-tracker.db
  (:require [hugsql.core :as hugsql]
            [hugsql.parameters]
            [hugsql-adapter-case.adapters :refer [kebab-adapter]]))

(def config
  {:classname "org.postgresql.Driver"
   :subprotocol "postgresql"
   :subname "//localhost:5432/mood_tracker"
   :user "postgres"
   :password "postgres"})

(hugsql/def-db-fns "sql/reports.sql" {:adapter (kebab-adapter)})
(hugsql/def-db-fns "sql/users.sql" {:adapter (kebab-adapter)})

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
