(ns mood-tracker.components.contact-form
  (:require [ajax.core :refer [PUT POST]]
            [helix.core :refer [defnc $ <>]]
            [helix.dom :as d]
            [helix.hooks :as hooks]
            [clojure.string]
            [mood-tracker.state :refer [use-app-state]]
            [mood-tracker.utils :refer [contact-form-fields make-label-str]]))

(defnc contact-display-item [{:keys [label value]}]
  (d/p
   (d/strong (make-label-str label))
   value))

(defnc contact-display [{:keys [user]}]
  (<>
   (map-indexed
    (fn [i v]
      ($ contact-display-item {:label v
                               :value (get user (keyword v))
                               :key i}))
      contact-form-fields)))

(defnc contact-edit-item [{:keys [label value on-change]}]
  (d/div
   (d/label {:for label
             :class '[font-bold]}
            (make-label-str label))
   (d/input {:id label
             :class '[shadow border rounded w-full py-2 px-3 mb-3]
             :value value
             :on-change on-change})))

(defnc contact-edit [{:keys [user]}]
  (let [[state set-state] (hooks/use-state user)
        [app-state actions] (use-app-state)
        selected (:selected app-state)
        {:keys [add-user update-user]} actions]
    (d/form {:on-sumbit (fn [e]
                          (.preventDefault e)
                          (if selected
                            (PUT (str "http://localhost:4000/api/contacts/" (:id selected))
                                 (let [{:keys [first_name last_name email]} state]
                                    {:params {:first-name first_name
                                            :last-name last_name
                                            :email email}
                                   :format :json
                                   :handler (fn [response]
                                              (update-user (:user response)))}))
                            (POST "http://localhost:4000/api/users"
                                  (let [{:keys [first_name last_name email]} state]
                                    {:params {:first-name first_name
                                            :last-name last_name
                                            :email email}
                                   :format :json
                                   :handler (fn [response]
                                              (add-user (first response)))}))
                            ))}
     (map-indexed
      (fn [i v]
        ($ contact-edit-item {:label v
                              :value (get state (keyword v))
                              :key i
                              :on-change #(set-state
                                           (assoc state
                                                  (keyword v)
                                                  (.. %
                                                      -target
                                                      -value)))}))
      contact-form-fields)
     (d/button {:type "submit"
                :class '[bg-teal-500 py-2 px-4 w-full text-white]}
               "Submit"))))

(defnc contact-form [{:keys [user]}]
  (let [[edit set-edit] (hooks/use-state false)
        [state actions] (use-app-state)
        user (:selected state)
        new-user (:new-user actions)]
    (hooks/use-effect
     [user]
     (if (not user)
       (set-edit true)
       (set-edit false)))
    (d/div
     (d/div {:class '[mb-2 flex justify-between]}
            (d/button {:class '[bg-teal-500 py-1 px-4 rounded text-white]
                       :on-click #(new-user)}
                      "New user")
            (d/button {:class '[bg-teal-500 py-1 px-4 rounded text-white]
                       :on-click #(set-edit (not edit))}
                      "Edit user"))

     (if edit
       ($ contact-edit {:user user})
       ($ contact-display {:user user}))
     )))
