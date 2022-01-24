(ns good-mood.reports-schemes
  (:require
   [malli.core :as m]
   [malli.transform :as mt]
   [malli.error :as me]
   [malli.util :as mu]))

(defn max-length [str-len]
  (fn [str]
    (<= (count str) str-len)))

(def new-report [:map
                 [:user-id :int]
                 [:comment [:and :string [:fn {:error/message "Комментарий должен быть не больше 30 символов"} (max-length 30)]]]
                 [:mood-grade [:and :int [:> 0] [:<= 10]]]
                 [:activity-grade [:and :int [:> 0] [:<= 10]]]
                 [:happiness-grade [:and :int [:> 0] [:<= 10]]]
                 [:details any?]
                 [:report-date string?]])

(comment

  (def aaa {:x true, :z "kikka", :y 6})
  (def test-scheme [:map
                    [:x :boolean]
                    [:y {:optional true} [:and :int [:< 10] [:> 5]]]
                    [:z :string]])

  (def t-s [:map
            [:comment [:string {:max 5}]]])

  (def ss (m/schema t-s))

  (m/validate ss {:comment "asdfasdfsdf"})

  (m/validate test-scheme aaa )

  (mu/subschemas new-report)
  def

  )
