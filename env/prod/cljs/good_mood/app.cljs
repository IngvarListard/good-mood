(ns good-mood.app
  (:require [good-mood.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
