(ns mood-tracker.users
  (:require [mood-tracker.db :as db]))

(defn get-users
  [_]
  {:status 200
   :body (db/get-users db/config)})

(defn create-user
  [{:keys [parameters]}]
  (println parameters)
  (let [data (:body parameters)
        created-user-id (db/insert-user db/config data)]
    {:status 201
     :body (db/get-user-by-id db/config created-user-id)}))

(defn get-user-by-id
  [{:keys [parameters]}]
  (let [id (:path parameters)]
    {:status 201
     :body (db/get-user-by-id db/config id)}))

(defn update-user
  [{:keys [parameters]}]
  (let [id (get-in parameters [:path :id])
        body (:body parameters)
        data (assoc body :id id)
        updated-count (db/update-user-by-id db/config data)]
    (if (= 1 updated-count)
      {:status 200
       :body (db/get-user-by-id db/config {:id id})}
      {:status 404
       :error (format "Error while updating: updated count is %s. Should be 1" updated-count)})))

(defn delete-user
  [{:keys [parameters]}]
  (let [id (:path parameters)
        before-deleted (db/get-user-by-id db/config id)
        deleted-count (db/delete-user db/config id)]
    (if (= 1 deleted-count)
      {:status 200
       :body {:deleted true
              :user before-deleted}}
      {:status 404
       :error (format "Error while deleting: deleted count is %s. Should be 1" deleted-count)})))

(comment
  (db/insert-user db/config {:first-name "Jong"
                             :last-name "Jung"
                             :email "shit@shit.com"
                             :is-active true}))
