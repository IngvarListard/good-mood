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
   {:db (assoc db ::loading true)
    :http-xhrio {:method :post
                 :uri "/api/reports"
                 :timeout 5000
                 :params report-params
                 :format (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::create-report-success]
                 :on-failure [::set-error]}}))

(rf/reg-event-fx
 ::get-user-report-schema
 (fn [{:keys [db]} [_ user-id]]
   (println "asdfasdfasdasdfasdf")
   {:db (update db ::loading inc)
    :http-xhrio {:method :get
                 :uri "/api/schemas"
                 :timeout 5000
                 :params {:user-id user-id}
                 :format (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::fill-user-schemas]
                 :on-failure [::set-error]}}))

;; db
(rf/reg-event-db
 ::set-error
 (fn [db [_ error]]
   (-> db
       (update ::loading dec)
       (assoc ::error error))))

(rf/reg-event-db
::fill-user-schemas
 (fn [db [_ {:keys [data]}]]

   (let [user-schemas (concat (::user-schemas db) data)
         unique-schemas (map first (vals (group-by :id user-schemas)))]

     (-> db
       (update ::loading dec)
       (assoc ::user-schemas unique-schemas)))))


(comment

  (conj nil [1 2 3])



  )

(rf/reg-event-db
 ::create-report-success
 (fn [db [_ {:keys [data]}]]
   (-> db
       (update ::loading dec)
       (assoc :common/reports (conj (:common/report db) data)))))

;; subscriptions
(rf/reg-sub
 ::user-schemas
 (fn [db _]
   (::user-schemas db)))
