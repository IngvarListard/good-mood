(ns good-mood.components.reports.schemas
  (:require
   [re-frame.core :as rf]
   [reagent.core :as r]
   [good-mood.reducers.reports :as reports]
   [reagent-mui.material.autocomplete :refer [autocomplete]]
   [reagent-mui.material.icon-button :refer [icon-button]]
   [reagent-mui.material.dialog :refer [dialog]]
   [reagent-mui.material.form-group :refer [form-group]]
   [reagent-mui.material.form-control-label :refer [form-control-label]]
   [reagent-mui.material.checkbox :refer [checkbox]]
   [reagent-mui.material.dialog-actions :refer [dialog-actions]]
   [reagent-mui.material.dialog-content :refer [dialog-content]]
   [reagent-mui.material.dialog-content-text :refer [dialog-content-text]]
   [reagent-mui.material.dialog-title :refer [dialog-title]]
   [reagent-mui.material.button :refer [button]]
   ["@mui/material/TextField" :default TextField]
   [reagent-mui.icons.add :refer [add] :rename {add add-icon}]))


(defn schema-select
  [set-value! selected user-schemas]
  (fn
      []
      [autocomplete
       {:render-input (fn [p] (r/create-element TextField p))
        :options (or @user-schemas [])
        :disable-portal true
        :get-option-label (fn [opt] (.-description opt))
        :is-option-equal-to-value (fn [opt val] (= (.-id opt) (.-id val)))
        :on-change set-value!
        :value @selected}]))


(comment
  [
   {:id 1, :detail-type-id 1, :schema
    [{:key "duration_seconds", :type "int", :display-name "Длительность"} {:key "times_of_day", :icon nil, :type "string", :choices [{:key "morning", :icon nil, :display-name "Утро"} {:key "day", :icon nil, :display-name "День"} {:key "evening", :icon nil, :display-name "Вечер"}], :display-name "Время суток"}]}
   {:id 2, :detail-type-id 2, :schema [{:key "duration_seconds", :type "int", :display-name "Длительность"} {:icon nil, :type "string", :choices [{:icon nil, :value "gym", :display-name "Зал"} {:incon nil, :value "swimming", :display-name "Плавание"} {:icon nil, :value "another", :display-name "Другое"}], :field-name "kind", :display-name "Тип тренеровки"}]}
   ]

  )


(defn schema-detail
  [detail]
  (let [checked? (r/atom false)
        on-change! (fn [& rest] (println "DATA" rest))]
    (fn []
      [form-group
       [form-control-label
        {:control (r/as-element [checkbox {:checked @checked?
                                           :on-change on-change!}])
         :label (str (:detail-type-id detail))}]])))

(defn schema-details
  [selected-schema]
  (println "details rendered" (:details @selected-schema))
  (if (not @selected-schema)
    [:div]
    [:div (map schema-detail (:details @selected-schema))]))


(defn add-details-dialog
  []
  (let [_ (rf/dispatch [::reports/get-user-report-schema])
        ;; TODO: SWAP TO FALSE
        opened? (r/atom true)
        user-schemas (rf/subscribe [::reports/user-schemas])
        selected (r/atom (first @user-schemas))
        ;; принимает 4 аргумента
        ;; см https://mui.com/api/autocomplete/ onChange
        set-value! #(reset! selected %2)
        open! #(reset! opened? true)
        close! #(reset! opened? false)
        add! #(println "add butt on plug")]
    (fn []
      [:div
       [icon-button {:edge "start"
                     :color "inherit"
                     :on-click open!
                     :aria-label "add-detail"
                     :variant "contained"
                     :size "medium"}
        [add-icon {:font-size "large"}]]

       [dialog {:open @opened?
                :on-close close!}

        [dialog-title "Here we go again"]

        [dialog-content
         [schema-select
          set-value!
          selected
          user-schemas]

         [schema-details selected]

         [dialog-content-text "Shitty dialog content text"]

         [dialog-actions
          [button
           {:variant "outlined"
            :color "primary"
            :on-click close!}
           "Cancel"]
          [button
           {:variant "contained"
            :color "primary"
            :on-click add!}
           "Save"]]]
        ]])))


(comment



(rf/dispatch [::reports/get-user-report-schema 666])


  )
