(ns mood-tracker.routes
  (:require [mood-tracker.users :refer [get-users
                                        create-user
                                        get-user-by-id
                                        update-user
                                        delete-user]]
            [schema.core :as s]))


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
