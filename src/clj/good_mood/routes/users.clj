(ns good-mood.routes.users
  (:require
   [good-mood.db.core :as db]
   [good-mood.middleware.base :refer [middlewares]]))

(defn get-users
  [_]
  {:status 200
   :body (db/get-users)})

(defn create-user!
  [{:keys [parameters]}]
  (let [data (:body parameters)
        created-user-id (db/create-user! data)]
    {:status 201
     :body (db/get-user created-user-id)}))

(defn get-user
  [{:keys [parameters]}]
  (let [id (:path parameters)]
    {:status 200
     :body (db/get-user id)}))

(defn update-user!
  [{:keys [parameters]}]
  (let [id (get-in parameters [:path :id])
        body (:body parameters)
        data (assoc body :id id)
        updated-count (db/update-user! data)]
    (if (= 1 updated-count)
      {:status 200
       :body (db/get-user {:id id})}
      {:status 404
       :error (format "Error while updating: updated count is %s. Should be 1" updated-count)})))


(defn delete-user!
  [{:keys [parameters]}]
  (let [id (:path parameters)
        before-deleted (db/get-user id)
        deleted-count (db/delete-user! id)]
    (if (= 1 deleted-count)
      {:status 200
       :body {:deleted true
              :user before-deleted}}
      {:status 404
       :error (format "Error while deleting: deleted count is %s. Should be 1" deleted-count)})))

(comment
  (db/create-user! {:first-name "Jong"
                    :last-name "Jung"
                    :email "shit@shit.com"
                    :is-active true}))

(defn users-routes
  []
  ["/users"
   ["/" {:get get-users
         :post {:parameters {:body {:first-name string?
                                    :last-name string?
                                    :email string?
                                    :pass string?}}
                :handler create-user!}}]
   ["/:id" {:parameters {:path {:id int?}}
            :get get-user
            :put {:parameters {:body {:first-name string?
                                      :last-name string?
                                      :email string?}}
                  :handler update-user!}
            :delete delete-user!}]])
