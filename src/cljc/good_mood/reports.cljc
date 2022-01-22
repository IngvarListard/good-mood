(ns good-mood.reports
  (:require [malli.core :as m]))


(def name-schema [:map {:closed true} [:first-name string?] [:last-name string?]])
(def sign-in-schema [:map [:name name-schema] [:email string?] [:password string?]])

(def new-report-schema [:map
                        [:user-id int?]
                        [:comment string?]
                        [:mood-grade int?]
                        [:activity-grade int?]
                        [:happiness-grade int?]
                        [:details any?]
                        [:report-date string?]])
