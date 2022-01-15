(ns good-mood.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [good-mood.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[good-mood started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[good-mood has shut down successfully]=-"))
   :middleware wrap-dev})
