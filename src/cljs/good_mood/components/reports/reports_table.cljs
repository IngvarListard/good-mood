(ns good-mood.components.reports.reports-table
  (:require
   [reagent-mui.material.table :refer [table]]
   [reagent-mui.material.table-body :refer [table-body]]
   [reagent-mui.material.table-cell :refer [table-cell]]
   [reagent-mui.material.table-container :refer [table-container]]
   [reagent-mui.material.table-head :refer [table-head]]
   [reagent-mui.material.table-row :refer [table-row]]))

(def cols ["Mood grade" "Activity grade" "Happiness grade" "Comment" "Details" "Report date"])

(defn display-reports-table [reports]
  [table-container
    [table {:aria-label "simple table"}
     [table-head
      [table-row
       (map-indexed (fn [i col-name] [table-cell {:key i} col-name]) cols)]]
     [table-body
      (map (fn [{:keys [id mood-grade activity-grade happiness-grade report-date details comment]}]
             [table-row {:key id}
              [table-cell {:component "th" :scope "row"} mood-grade]
              [table-cell {:align "right"} activity-grade]
              [table-cell {:align "right"} happiness-grade]
              [table-cell {:align "right"} comment]
              [table-cell {:align "right"} details]
              [table-cell {:align "right"} report-date]])
           reports)]]])
