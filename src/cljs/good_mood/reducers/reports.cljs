(ns good-mood.reducers.reports
  (:require
    [re-frame.core :as rf]
    [ajax.core :as ajax]
    [reitit.frontend.easy :as rfe]
    [reitit.frontend.controllers :as rfc]
    [good-mood.reducers.base :as base]))

;; dispatch
(rf/reg-event-fx
 ::create-report
 (fn [{:keys [db]} [_ report-params]]
   (println report-params)
   {:db (assoc db ::loading true)
    :http-xhrio {:method :post
                 :uri "/api/reports"
                 :timeout 5000
                 :params report-params
                 :format (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::create-user-success]
                 :on-failure [::set-error]}}))


;; db
(rf/reg-event-db
  ::set-error
  (fn [db [_ error]]
    (assoc db ::error error)))

(rf/reg-event-db
 ::create-user-success
 (fn [db [_ {:keys [data]}]]
   (-> db
       (assoc ::loading false)
       (assoc :common/reports (conj (:common/report db) data)))))
