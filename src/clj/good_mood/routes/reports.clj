(ns good-mood.routes.reports
  (:require
   [good-mood.db.core :as db]
   [good-mood.middleware.base :refer [middlewares]]
   [muuntaja.core :as m]
   [reitit.coercion.spec :as spec-coercion]
   [good-mood.middleware.formats :as formats]))

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
   :body (db/get-reports)})

(defn get-report-by-id
  [{:keys [parameters]}]
  (let [id (:path parameters)]
    {:status 200
     :body (db/get-report-by-id id)}))

(defn create-report
  [{:keys [parameters]}]
  (println parameters)
  (let [data (:body parameters)
        report-id (->> data
                    :details
                    (m/encode "application/json")
                    slurp
                    (assoc data :details)
                    (db/create-report!))]
    (println "REPORTID" report-id)
    {:status 201
     :body (db/get-report-by-id report-id)}))

(defn update-report
  [{:keys [parameters]}]
  (let [id (get-in parameters [:path :id])
        body (:body parameters)
        data (assoc body :id id)
        updated-count (db/update-report-by-id! (encode-json-fields data :details))]

    (if (= 1 updated-count)
      {:status 200
       :body (db/get-report-by-id {:id id})}
      {:status 404
       :error (format "Error while updating: updated count is %s. Should be 1" updated-count)})
    ))

(defn delete-report
  [{:keys [parameters]}]
  (let [id (:path parameters)
        before-deleted (db/get-report-by-id id)
        deleted-count (db/delete-report-by-id! id)]
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

(defn reports-routes
  []
  ["/reports"
    ["/"
     {:get get-reports
      :post {:parameters {:body {:user-id int?
                                 :comment string?
                                 :mood-grade int?
                                 :activity-grade int?
                                 :happiness-grade int?
                                 :details any?
                                 :report-date string?}}
             :handler create-report}}]
    ["/:id"
     {:parameters {:path {:id int?}}
      :get get-report-by-id
      :put {:parameters {:body {:user-id int?
                                :comment string?
                                :mood-grade int?
                                :activity-grade int?
                                :happiness-grade int?
                                :details any?
                                :report-date string?}}
            :handler update-report}
      :delete delete-report}]])
