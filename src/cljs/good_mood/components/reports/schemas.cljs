(ns good-mood.components.reports.schemas
  (:require
   [re-frame.core :as rf]
   [reagent.core :as r]
   [good-mood.reducers.reports :as reports]
   [reagent-mui.material.autocomplete :refer [autocomplete]]
   [reagent-mui.material.icon-button :refer [icon-button]]
   [reagent-mui.material.dialog :refer [dialog]]
   [reagent-mui.material.dialog-actions :refer [dialog-actions]]
   [reagent-mui.material.dialog-content :refer [dialog-content]]
   [reagent-mui.material.dialog-content-text :refer [dialog-content-text]]
   [reagent-mui.material.dialog-title :refer [dialog-title]]
   [reagent-mui.material.button :refer [button]]
   ["@mui/material/TextField" :default TextField]
   [reagent-mui.icons.add :refer [add] :rename {add add-icon}]))

(defn add-details-dialog
  []
  (let [_ (rf/dispatch [::reports/get-user-report-schema])
        opened? (r/atom false)
        selected (r/atom nil)
        on-change! #(reset! selected %)
        open! #(reset! opened? true)
        close! #(reset! opened? false)
        save! #(println "save butt on plug")]
    (fn []
      (let [user-schemas (rf/subscribe [::reports/user-schemas])]
        [:div
         [icon-button
          {:edge "start"
           :color "inherit"
           :on-click open!
           :aria-label "add-detail"
           :variant "contained"
           :size "medium"}
          [add-icon
           {:font-size "large"}]]
         [dialog
          {:open @opened?
           :on-close close!}

          [dialog-title
           "Here we go again"]

          [dialog-content
           [dialog-content-text
            [autocomplete
             {:render-input (fn [p] (r/create-element TextField p))
              :options @user-schemas
              :disable-portal true}]
            "Shitty dialog content text"]

           [dialog-actions

            [button
             {:variant "outlined"
              :color "primary"
              :on-click close!}
             "Cancel"]

            [button
             {:variant "contained"
              :color "primary"
              :on-click close!}
             "Save"]]]
          ]]))))

(comment



(rf/dispatch [::reports/get-user-report-schema 666])


  )
