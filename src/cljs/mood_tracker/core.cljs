(ns mood-tracker.core
  (:require [ajax.core :refer [GET]]
            [helix.core :refer [defnc $ <> provider]]
            [helix.hooks :as hooks]
            [helix.dom :as d]
            ["react-dom" :as dom]
            [mood-tracker.state :refer [app-state initial-state app-reducer use-app-state]]
            [mood-tracker.components.nav :refer [nav]]
            [mood-tracker.components.contact-list :refer [contact-list]]
            [mood-tracker.components.contact-form :refer [contact-form]]))

(defnc app []
  (let [[state actions] (use-app-state)
        init (:init actions)]
    (hooks/use-effect
     :once
     (GET "http://localhost:4000/api/users/"
          {:handler init}))
    (js/console.log state)
    (if state
      (<>
       ($ nav)
       (d/div {:class '[container pt-4]}
              ($ contact-list)
              ($ contact-form)))
      (d/p "Loading..."))))

(defnc app-provider []
  (provider {:context app-state
             :value (hooks/use-reducer app-reducer initial-state)}
   ($ app)))

(defn ^:export ^:dev/after-load init []
  ;; after-load - код запускается каждый раз когда мы сохраняем наш код он
  ;; обновляется в нашем браузере
  (dom/render ($ app-provider) (js/document.getElementById "app")))
