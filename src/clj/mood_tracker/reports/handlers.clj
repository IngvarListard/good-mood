(ns mood-tracker.reports.handlers
  (:require [mood-tracker.db :as db])
  (:require [muuntaja.core :as m]))

(defn get-reports
  ;; пилим сначала без пагинации
  [_]
  {:status 200
   :body (db/get-reports db/config)})

(defn get-report-by-id [] {})

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

(defn update-report [] {})

(defn delete-report [] {})


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
