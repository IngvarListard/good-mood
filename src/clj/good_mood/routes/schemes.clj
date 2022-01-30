(ns good-mood.routes.schemes
  (:require
   [good-mood.db.core :as db]
   [muuntaja.core :as m]))

(defn get-schemas-for-user
  [{:keys [user-id]}]
  (let [schemas (db/get-schemas)]
    ;; больше так делать не буду. рефачить лень.
    {:status 200
     :body {:data (->> schemas
                       (mapcat :detail-items-ids)
                       (#(db/get-details-by-ids {:ids %}))
                       (map #(do {(:id %) %}))
                       (apply merge)
                       ((fn [grouped]
                          (map (fn [{ids :detail-items-ids :as schema}]
                                 (assoc schema :details (map grouped ids)))
                               schemas))))}}))

(comment

(get-schemas-for-user {})

  (db/get-schema)

(let [schemas (db/get-schemas)]
    (->> schemas
         (mapcat :detail-items-ids)
         (#(db/get-details-by-ids {:ids %}))
         (map #(do {(:id %) %}))
         (apply merge)
         ((fn [grouped]
            (map (fn [{ids :detail-items-ids :as schema}]
                   (assoc schema :details (map grouped ids)))
                 schemas)))))


   ;; (let [data '({:id 1, :detail-items-ids [1 2], :description "test 1", :created-by nil}
   ;;             {:id 2, :detail-items-ids [2 3], :description "test 2", :created-by nil}
   ;;             {:id 3, :detail-items-ids [3 4], :description "test 3", :created-by nil})]
   ;;  (zipmap (map :id data) data))

(map #(do {(:id %) %}) [{:id 1} {:id 2}])
(map #(constantly {(:id %) %}))
(map #(hash-map (:id %) %) [{:id 1} {:id 2}])

(let [schemas (db/get-schemas)]
    (->> schemas
         (group-by :detail-items-ids)
         ))



;; :parameters {:body [:map [:user-id {:optional true} any?]]}
)

(defn schemas-routes
  []
  ["/schemas"
   ["/"
    {:get get-schemas-for-user}]])
