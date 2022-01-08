(ns mood-tracker.state
  (:require [helix.core :refer [create-context]]
            [helix.hooks :as hooks]))

(def initial-state {:selected nil
                    :users []})

(def app-state (create-context nil))

(defn use-app-state []
  (let [[state dispatch] (hooks/use-context app-state)]
    [state {:init (fn [response]
                    (dispatch [::set-users response])
                    (dispatch [::set-selected (first response)]))
            :select #(dispatch [::set-selected %])
            :new-user #(dispatch [::set-selected nil])
            :add-user #(dispatch [::add-user %])
            :update-user #(dispatch [::update-user %])
            :remove-user #(dispatch [::remove-user %])}]))

(defmulti app-reducer
  (fn [_ action]
    (first action)))

(defmethod app-reducer
  ::set-users [state [_ payload]]
  (assoc state :users payload))

(defmethod app-reducer
  ::set-selected [state [_ payload]]
  (assoc state :selected payload))

(defmethod app-reducer
  ::add-user [state [_ payload]]
  (let [prev (:users state)
        next (conj prev payload)]
    (assoc state :users next)))

(defmethod app-reducer
  ::remove-user [state [_ payload]]
  (let [prev (:users state)
        not-match #(not= (:id %) (:id payload))
        next (filter not-match prev)]
    (assoc state :users next)))

(defmethod app-reducer
  ::update-user [state [_ payload]]
  (let [prev (:users state)
        update-users #(if (= (:id %) (:id payload))
                        payload
                        %)
        next (map update-users prev)]
    (assoc state :users next)))
