(ns mood-tracker.components.contact-list
  (:require [ajax.core :refer [DELETE]]
            [helix.core :refer [defnc <> $]]
            [helix.dom :as d]
            [mood-tracker.state :refer [use-app-state]]))

(defnc contact-list-item [{:keys [user]}]
  (let [[_ actions] (use-app-state)
        set-selected (:select actions)
        remove-user (:remove-user actions)]
    (d/li {:class '[mb-2]}
        (d/div {:class '[flex justify-between]}
               (d/p (str (:first_name user) " " (:last_name user)))
               (d/div
                (d/button {:class '[bg-teal-500 text-white py-1 px-4 rounded]
                           :on-click #(set-selected user)}
                          "Select")
                (d/button {:class '[bg-red-500 ml-2 text-white py-1 px-4 rounded]
                           :on-click #(DELETE (str "http://localhost:4000/api/users/" (:id user))
                                              {:handler (fn [_]
                                                          (remove-user user))})}
                          "Delete"))))))

(defnc contact-list []
  (let [[state _] (use-app-state)
        users (:users state)]
    (<>
     (d/ul
      (map-indexed (fn [i user]
                     ($ contact-list-item {:key i
                                           :user user}))
                   users)))))
