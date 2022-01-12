(ns mood-tracker.reports.handlers
  (:require [mood-tracker.db :as db])
  (:require [muuntaja.core :as m]))

(defn encode-json-fields
  [obj field]
  (->> obj
       (field)
       (m/encode "application/json")
       slurp
       (assoc obj field)))

(defn get-reports
  ;; пилим сначала без пагинации
  [_]
  {:status 200
   :body (db/get-reports db/config)})

(defn get-report-by-id
  [{:keys [parameters]}]
  (let [id (:path parameters)]
    (println "get report parameters", parameters)
    {:status 200
     :body (db/get-report-by-id db/config id)}))

(defn create-report
  [{:keys [parameters]}]
  (let [data (:body parameters)
        report-id (->> data
                    :details
                    (m/encode "application/json")
                    slurp
                    (assoc data :details)
                    (db/insert-report db/config))]
    {:status 201
     :body (db/get-report-by-id db/config report-id)}))

(defn update-report
  [{:keys [parameters]}]
  (let [id (get-in parameters [:path :id])
        body (:body parameters)
        data (assoc body :id id)
        updated-count (db/update-report-by-id db/config (encode-json-fields data :details))]

    (if (= 1 updated-count)
      {:status 200
       :body (db/get-report-by-id db/config {:id id})}
      {:status 404
       :error (format "Error while updating: updated count is %s. Should be 1" updated-count)})
    ))

(defn delete-report
  [{:keys [parameters]}]
  (let [id (:path parameters)
        before-deleted (db/get-report-by-id db/config id)
        deleted-count (db/delete-report-by-id db/config id)]
    (if (= 1 deleted-count)
      {:status 200
       :body {:deleted true
              :report before-deleted}}
      {:status 404
       :error (format "Error while deleting: deleted count is %s. Should be 1" deleted-count)})))


(comment
  (let [a (assoc ddd :details (slurp (m/encode "application/json" (:details ddd))))]
    (println a)
    (db/insert-report db/config a))


  (let [report-id (->> ddd
                    :details
                    (m/encode "application/json")
                    slurp
                    (assoc ddd :details)
                    (db/insert-report db/config))]
    {:status 201
     :body (db/get-report-by-id db/config report-id)})

  )
