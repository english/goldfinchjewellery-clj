(ns goldfinchjewellery.views.session
  (:require [goldfinchjewellery.views.layout :as layout]
            [clojure.string :refer [capitalize]]
            [hiccup.form :refer [email-field password-field submit-button]]))

(defn control [field name placeholder]
  (let [capitalized (capitalize name)]
    [:div.form-group
     [:label.col-sm-2.control-label {:for name} capitalized]
     [:div.col-sm-10
      (field {:class "form-control" :placeholder placeholder} name)]]))

(defn sessions-new [& [email password error]]
  (layout/common
    [:h1 "Log In "]
    (when error [:p.error error])
    [:form.form-horizontal {:action "/sessions" :method "POST"}
     (control email-field "email" "someone@example.com")
     (control password-field "password" "a super secret password")
     [:div.form-group
      [:div.col-sm-offset-2.col-sm-10
       (submit-button {:class "btn btn-default"} "Sign in")]]]))
