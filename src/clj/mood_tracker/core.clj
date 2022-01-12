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
            [mood-tracker.routes :refer [ping-routes users-routes reports-routes]]
            [mood-tracker.middleware :refer [letter-case-request letter-case-response]]
            [camel-snake-kebab.core :as csk]))


(defonce server (atom nil))

(defn stop-server []
  ;; Короче когда в значении атома не нулевое значение тогда мы разыменовываем переменную
  ;; атом server и возвращаем значение. Иначе просто делаем reset! - ставим значение атома в nil
  (when-not (nil? @server)
    (@server :timeout 100)
    (println "Stopping server")
    (reset! server nil)))

;; (def m (m/create
;;         (assoc-in
;;          m/default-options
;;          [:formats "application/json" :encoder-opts]
;;          {:encode-key-fn csk/->kebab-case})))

(def app
  (ring/ring-handler
   (ring/router
    ["/api"
     ping-routes
     users-routes
     reports-routes]
    {:data {:coercion reitit.coercion.schema/coercion
            :muuntaja m/instance
            :middleware [[wrap-cors
                          :access-control-allow-origin [#"http://localhost:4200"]
                          :access-control-allow-methods [:get :post :put :delete]]
                         parameters-middleware
                         format-negotiate-middleware
                         format-request-middleware
                         ;;[letter-case-request {:from :snake_case :to :kebab-case}]
                         ;; exception-middleware
                         format-response-middleware
                         coerce-exceptions-middleware
                         coerce-request-middleware
                         ;;[letter-case-response {:to :snake_case}]
                         coerce-response-middleware]}})

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

(comment
  (restart-server)
  (stop-server)
  @server
  (-main)
  (app {:request-method :get
        :uri "/api/ping"})
  (app {:request-method :get :uri "/api/users"}))
