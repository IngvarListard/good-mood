(ns good-mood.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[good-mood started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[good-mood has shut down successfully]=-"))
   :middleware identity})
