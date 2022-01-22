(ns good-mood.reports
  (:require
   [malli.core :as m]
   [malli.transform :as mt]))


;; (def name-schema [:map {:closed true} [:first-name string?] [:last-name string?]])
;; (def sign-in-schema [:map [:name name-schema] [:email string?] [:password string?]])

;; (def new-report-schema [:map
;;                         [:user-id int?]
;;                         [:comment string?]
;;                         [:mood-grade int?]
;;                         [:activity-grade int?]
;;                         [:happiness-grade int?]
;;                         [:details any?]
;;                         [:report-date string?]])

;; (comment

;;   (m/validate name-schema {:first-name "asdf" :last-name "asfasdf"})



;;   (m/validate (m/schema :int) 1)

;; (m/decode int? "42" mt/string-transformer)

;; (m/decode
;;   [string? {:decode/shit 'str/upper-case}]
;;   "kerran" mt/transformer {:name :shit})


;; (m/decode
;;   [:map
;;    {:decode/shit {:enter '#(update % :x inc)
;;                   :leave '#(update % :x (partial * 2))
;;                   }}
;;    [:x [int? {:decode/shit {:enter '(partial + 2)
;;                             :leave '(partial * 3)}}]]]
;;   {:x 1}
;;   (mt/transformer {:name :shit}))

;; (m/decode
;;   [string? {:decode {:string '#(constantly 1)}}]
;;   "kerran" mt/string-transformer)

;; (m/decode
;;   [string? {:decode/string '(fn [_] 1)}]
;;   "kerran" mt/string-transformer)
;;   )
