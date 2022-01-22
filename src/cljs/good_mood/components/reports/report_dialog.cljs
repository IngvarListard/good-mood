(ns good-mood.components.reports.report-dialog
  (:require-macros [reagent-mui.util :refer [react-component]])
  (:require
   [reagent.core :as r]
   [re-frame.core :as rf]
   [react]
   [good-mood.reducers.reports :as reports]
   [reagent-mui.material.dialog :refer [dialog]]
   [reagent-mui.material.button :refer [button]]
   [reagent-mui.material.icon-button :refer [icon-button]]
   [reagent-mui.material.slide :refer [slide]]
   [reagent-mui.material.divider :refer [divider]]
   [reagent-mui.material.app-bar :refer [app-bar]]
   [reagent-mui.material.toolbar :refer [toolbar]]
   [reagent-mui.material.typography :refer [typography]]
   [reagent-mui.material.slider :refer [slider]]
   [reagent-mui.material.container :refer [container]]
   [reagent-mui.material.text-field :refer [text-field]]
   ["@mui/lab/DatePicker" :default DatePicker]
   ["@mui/lab/LocalizationProvider" :default LocalizationProvider]
   ["@mui/lab/AdapterDateFns" :default AdapterDateFns]
   ["@mui/material/TextField" :default TextField]
   [reagent-mui.icons.close :refer [close] :rename {close close-icon}]))

(defn event-value
  [e]
  (.. e -target -value))

(def transition
  "не работает
  https://github.com/reagent-project/reagent/blob/c214466bbcf099eafdfe28ff7cb91f99670a8433/examples/react-mde/src/example/core.cljs
  forward-ref example"
  (react/forwardRef
    (fn [props ref]
      (let [props (assoc (js->clj props) :ref ref)]
        (set! (.-direction props) "up")
        (r/create-element slide props)))))


(defn top-bar [{:keys [close save]}]
  [app-bar {:sx {:position "relative"}}
   [toolbar
    [icon-button
     {:edge "start"
      :color "inherit"
      :on-click close
      :aria-label "close"}
     [close-icon]]
    [typography
     {:sx {:ml 2 :flex 1}
      :variant "h6"
      :component "div"}
     "Sound"]
    [button
     {:auto-focus true
      :color "inherit"
      :on-click save}
     "Save"]]])

(defn grade-slider [{:keys [aria-label value on-change]}]
  [slider
   {:aria-label aria-label
    :value value
    :on-change on-change
    :default-value 7
    :value-label-display "auto"
    :step 1
    :marks true
    :min 1
    :max 10}])

(defn dialog-form [{:keys [value on-change on-text-change on-date-change]}]
  ;; слайдеры
  [:div
   [grade-slider
    {:aria-label "Настроение"
     :value (value :mood-grade)
     :on-change (on-change :mood-grade)}]
   [grade-slider
    {:aria-label "Активность"
     :value (value :activity-grade)
     :on-change (on-change :activity-grade)}]
   [grade-slider
    {:aria-label "Счастье"
     :value (value :happiness-grade)
     :on-change (on-change :happiness-grade)}]
   [divider]

   [:div
    [text-field
     {:multiline true
      :value (value :comment)
      :on-change (on-text-change :comment)
      :aria-label "Комментарий"}]]

   [:div
    [:> LocalizationProvider
     {:dateAdapter AdapterDateFns}
     [:> DatePicker
      {:label "Report date"
       :value (value :report-date)
       :on-change (on-date-change :report-date)
       :render-input (fn [p] (r/create-element TextField p))}]]]])

(comment

  (let [d (new js/Date)
        t (.toISOString d)]
    (println t)
    t)
  )

(defn create-report-dialog []
  (let [opened? (r/atom false)
        open #(reset! opened? true)
        close #(reset! opened? false)
        report-form (r/atom
                      {:user-id 1
                       :mood-grade 7
                       :activity-grade 7
                       :happiness-grade 7
                       :details nil
                       :report-date nil
                       :comment "asdfasdf"})
        on-change (fn [^Keyword field]
                    (fn [_ val]
                      (swap! report-form assoc field val)))
        on-text-change (fn [^Keyword field]
                         (fn [e]
                           (swap! report-form assoc field (event-value e))))
        on-date-change (fn [^Keyword field]
                         (fn [val]
                           (println val @report-form)
                           (swap! report-form assoc field (.toISOString val))))
        value #(get @report-form %)
        save #(rf/dispatch [::reports/create-report @report-form])]

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

        [top-bar
         {:close close
          :save save}]

        [container
         {:max-width "xl"}

         [dialog-form
          {:value value
           :on-change on-change
           :on-text-change on-text-change
           :on-date-change on-date-change}]]]])))
