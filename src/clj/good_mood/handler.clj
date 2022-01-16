(ns good-mood.handler
  (:require
    [good-mood.middleware :as middleware]
    [good-mood.layout :refer [error-page]]
    [good-mood.routes.home :refer [home-routes]]
    [good-mood.routes.services :refer [service-routes]]
    [good-mood.routes.reports :refer [reports-routes]]
    [good-mood.routes.users :refer [users-routes]]
    [reitit.swagger-ui :as swagger-ui]
    [reitit.ring :as ring]
    [ring.middleware.content-type :refer [wrap-content-type]]
    [ring.middleware.webjars :refer [wrap-webjars]]
    [good-mood.env :refer [defaults]]
    [mount.core :as mount]
    [reitit.ring.coercion :as coercion]
    [reitit.coercion.spec :as spec-coercion]
    [reitit.ring.middleware.muuntaja :as muuntaja]
    [reitit.ring.middleware.multipart :as multipart]
    [reitit.ring.middleware.parameters :as parameters]
    [good-mood.middleware.formats :as formats]))

(mount/defstate init-app
  :start ((or (:init defaults) (fn [])))
  :stop  ((or (:stop defaults) (fn []))))

(mount/defstate app-routes
  :start
  (ring/ring-handler
   (ring/router
    [(home-routes)
     ["/api"
      {:coercion spec-coercion/coercion
       :muuntaja formats/instance
       :swagger {:id ::api}
       :middleware [ ;; query-params & form-params
                    parameters/parameters-middleware
                    ;; content-negotiation
                    muuntaja/format-negotiate-middleware
                    ;; encoding response body
                    muuntaja/format-response-middleware
                    ;; exception handling
                    coercion/coerce-exceptions-middleware
                    ;; decoding request body
                    muuntaja/format-request-middleware
                    ;; coercing response bodys
                    coercion/coerce-response-middleware
                    ;; coercing request parameters
                    coercion/coerce-request-middleware
                    ;; multipart
                    multipart/multipart-middleware]}
      [(service-routes)
       (reports-routes)
       (users-routes)]]])
   (ring/routes
    (swagger-ui/create-swagger-ui-handler
     {:path   "/swagger-ui"
      :url    "/api/swagger.json"
      :config {:validator-url nil}})
    (ring/create-resource-handler
     {:path "/"})
    (ring/redirect-trailing-slash-handler)
    (wrap-content-type
     (wrap-webjars (constantly nil)))
    (ring/create-default-handler
     {:not-found
      (constantly (error-page {:status 404, :title "404 - Page not found"}))
      :method-not-allowed
      (constantly (error-page {:status 405, :title "405 - Not allowed"}))
      :not-acceptable
      (constantly (error-page {:status 406, :title "406 - Not acceptable"}))}))))

(defn app []
  (middleware/wrap-base #'app-routes))
