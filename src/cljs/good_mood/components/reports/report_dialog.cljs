(ns good-mood.components.reports.report-dialog
  (:require
   [reagent.core :as r]
   [reagent-mui.material.dialog :refer [dialog]]))

(defn display-dialog [dialog-opened?]
  (fn []
      (println "dialog rendered")
    (let [opened? @dialog-opened?]
      (println "dialog rendered")
      [dialog
       {:full-screen true
        :open opened?}
       [:div "Hey yo"]])))
