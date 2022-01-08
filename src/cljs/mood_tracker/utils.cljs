(ns mood-tracker.utils
  (:require [clojure.string :as string]))


(def contact-form-fields ["first_name" "last_name" "email"])

(defn make-label-str [s]
  (str (-> s
           (string/replace "_" " ")
           string/capitalize)
       ": "))
