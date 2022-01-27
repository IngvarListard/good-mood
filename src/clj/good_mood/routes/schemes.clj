(ns good-mood.routes.schemes
  (:require
   [good-mood.db.core :as db]
   [muuntaja.core :as m]))

(defn get-schemas-for-user
  [uid]

  (let [schemas (db/get-schemas)]
    (->> schemas
         (mapcat :detail-items-ids)
         (#(db/get-details-by-ids {:ids %}))
         (map #(do {(:id %) %}))
         (apply merge)
         ((fn [grouped]
            (map (fn [{ids :detail-items-ids :as schema}]
                   (assoc schema :details (map grouped ids)))
                 schemas))))))


(comment

(get-schemas-for-user 100)

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
         )
;; так что нужно сделать
;; объединить массивы данных
;; в одном есть [1 2] { } [3 4] {  }
;; в другом есть [1 2 3 4]
;; значит нужно пройтись по всем хуйням вторым
;; не, надо пройтись по всем первым
;; иду я по ним и ищу в массиве пересечения
;; а мне надо вывернуть значит по id
;; выворачиваю значит по id получается
;; {1: {}
;; 2: {}
;; 3: {}}
;; И я такой беру прохожусь по всем первым, а потом по их
;; элементам
;; цикл в цикле получается

    )




)
