(ns good-mood.pages.reports.index
  (:require
   [good-mood.components.reports.reports-table :refer [display-reports-table]]
   [good-mood.components.reports.report-dialog :refer [create-report-dialog]]
   [re-frame.core :as rf]
   [reagent.core :as r]
   [reagent-mui.material.button :refer [button]]
   [reagent-mui.material.paper :refer [paper]]))


(defn reports-page []
  (let [loading (rf/subscribe [:common/loading])
        reports (rf/subscribe [:common/reports])]
    [:section.section>div.container>div.content
       [button {:variant "contained"
                :color "primary"
                :on-click #(rf/dispatch [:common/fetch-reports])}
        "Get reports"]
       [create-report-dialog]
       (when @loading [:div "Loading..."])
       [paper (display-reports-table @reports)]]))


(comment

  (let [dialog? (r/atom false)]
    (println "DIALOG 1" @dialog?)
    (reset! dialog? true)
    (println "DIALOG 2" @dialog?)
))
