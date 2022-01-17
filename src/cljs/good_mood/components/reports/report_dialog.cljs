(ns good-mood.components.reports.report-dialog
  (:require
   [reagent.core :as r]
   [react]
   [reagent-mui.material.dialog :refer [dialog]]
   [reagent-mui.material.button :refer [button]]
   [reagent-mui.material.icon-button :refer [icon-button]]
   [reagent-mui.material.dialog-title :refer [dialog-title]]
   [reagent-mui.material.slide :refer [slide]]
   [reagent-mui.material.divider :refer [divider]]
   [reagent-mui.material.app-bar :refer [app-bar]]
   [reagent-mui.material.toolbar :refer [toolbar]]
   [reagent-mui.material.typography :refer [typography]]
   [reagent-mui.icons.close :refer [close] :rename {close close-icon}]

   ))

(def transition
  "не работает
  https://github.com/reagent-project/reagent/blob/c214466bbcf099eafdfe28ff7cb91f99670a8433/examples/react-mde/src/example/core.cljs
  forward-ref example"
  (react/forwardRef
    (fn [props ref]
      (let [props (assoc (js->clj props) :ref ref)]
        (set! (.-direction props) "up")
        (r/create-element slide props)))))

(defn top-bar [{:keys [close]}]
  [app-bar {:sx {:position "relative"}}
   [toolbar
    [icon-button {:edge "start"
                  :color "inherit"
                  :on-click close
                  :aria-label "close"}
     [close-icon]]
    [typography {:sx {:ml 2 :flex 1}
                 :variant "h6"
                 :component "div"}
     "Sound"]
    [button {:auto-focus true
             :color "inherit"
             :on-click close}
     "Save"]]])

(defn dialog-body [])

(defn create-report-dialog []
  (let [opened? (r/atom false)
        open #(reset! opened? true)
        close #(reset! opened? false)]
    (fn []
      (println "dialog rendered" @opened?)
      [:div
       [button {:variant "contained"
                :color "primary"
                :on-click open
                :on-close close}
        "Create report"]
       [dialog
        {:full-screen true
         :open @opened?}
        [top-bar {:close close}]
        [:div "Hey yo"]
        ]])))
