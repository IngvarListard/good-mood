(ns mood-tracker.core
  (:require [org.httpkit.server :refer [run-server]]
            [reitit.ring :as ring]
            [ring.middleware.cors :refer [wrap-cors]]
            [reitit.ring.middleware.exception :refer [exception-middleware]]
            [reitit.ring.middleware.parameters :refer [parameters-middleware]]
            [reitit.ring.middleware.muuntaja :refer [format-negotiate-middleware
                                                     format-request-middleware
                                                     format-response-middleware]]
            [reitit.ring.coercion :refer [coerce-exceptions-middleware
                                          coerce-request-middleware
                                          coerce-response-middleware]]
            [reitit.coercion.schema]
            [muuntaja.core :as m]
            [mood-tracker.routes :refer [ping-routes users-routes]]))


(defonce server (atom nil))

(defn stop-server []
  ;; Короче когда в значении атома не нулевое значение тогда мы разыменовываем переменную
  ;; атом server и возвращаем значение. Иначе просто делаем reset! - ставим значение атома в nil
  (when-not (nil? @server)
    (@server :timeout 100)
    (println "Stopping server")
    (reset! server nil)))

(def app
  (ring/ring-handler
   (ring/router
    ["/api"
     ping-routes
     users-routes]
    {:data {:coercion reitit.coercion.schema/coercion
            :muuntaja m/instance
            :middleware [[wrap-cors
                          :access-control-allow-origin [#"http://localhost:4200"]
                          :access-control-allow-methods [:get :post :put :delete]]
                         parameters-middleware
                         format-negotiate-middleware
                         format-request-middleware
                         exception-middleware
                         format-response-middleware
                         coerce-exceptions-middleware
                         coerce-request-middleware
                         coerce-response-middleware
                         ]}})
   (ring/routes
    (ring/redirect-trailing-slash-handler)
    (ring/create-default-handler
     {:not-found (constantly {:status 404
                              :body "Route not found"})}))))

(defn -main []
  (println "Server started!")
  ;; как только функция run-server завершается, мы делаем reset
  ;; в переменную server значением, которое вернулось из функции
  ;; run-server
  (reset! server (run-server app {:port 4000})))

(defn restart-server []
  (println "Restart Server")
  (stop-server)
  (-main))

;; Вопросы
;; - что делает :refer в require
;; - defonce что это и зачем
;; - как работает when-not. Очевидно, но нужны пруфы
;; - как работают мультиметоды. Прочитать подробнее
(comment
  (restart-server)
  @server
  (-main)
  (app {:request-method :get
        :uri "/api/ping"})
  (app {:request-method :get :uri "/api/users"}))
