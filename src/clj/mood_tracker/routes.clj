(ns mood-tracker.routes
  (:require [mood-tracker.users :refer [get-users
                                        create-user
                                        get-user-by-id
                                        update-user
                                        delete-user]]
            [schema.core :as s]
            [mood-tracker.reports.handlers :refer [get-reports
                                                   get-report-by-id
                                                   update-report
                                                   create-report
                                                   delete-report]]))


(def ping-routes
  ["/ping" {:get (fn [req]
                     {:status 200
                      :body {:hello "pong"}})}])


(def users-routes
  ["/users"
   ["/" {:get get-users
         :post {:parameters {:body {:first-name s/Str
                                    :last-name s/Str
                                    :email s/Str
                                    :is-active s/Bool}}
                :handler create-user}}]
   ["/:id" {:parameters {:path {:id s/Int}}
            :get get-user-by-id
            :put {:parameters {:body {:first-name s/Str
                                      :last-name s/Str
                                      :email s/Str
                                      :is-active s/Bool}}
                  :handler update-user}
            :delete delete-user}]])


(def dummy (fn [& rest] nil))

(def reports-routes
  ["/reports"
   ["/" {:get get-reports
         :post {:parameters {:body {:user-id s/Int
                                    :comment s/Str
                                    :mood-grade s/Int
                                    :activity-grade s/Int
                                    :happiness-grade s/Int
                                    :details s/Any
                                    :report-date s/Str}}
                :handler create-report}
         }]
   ["/:id" {:parameters {:path {:id s/Int}}
            :get dummy
            :put dummy
            :delete dummy}]])
