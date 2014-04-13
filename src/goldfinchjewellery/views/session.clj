(ns goldfinchjewellery.views.session
  (:require [goldfinchjewellery.views.layout :as layout]
            [hiccup.form :refer [email-field password-field submit-button]]))

(defn sessions-new [& [email password error]]
  (layout/common
    [:h1 "Log In "]
    (when error [:p.error error])
    [:form.form-horizontal {:action "/sessions" :method "POST"}
     [:div.form-group
      [:label.col-sm-2.control-label {:for "email"} "Email"]
      [:div.col-sm-10
       (email-field {:class "form-control" :placeholder "Enter email"} "email")]]
     [:div.form-group
      [:label.col-sm-2.control-label {:for "password"} "Password"]
      [:div.col-sm-10
       (password-field {:class "form-control" :placeholder "Enter password"} "password")]]
     [:div.form-group
      [:div.col-sm-offset-2.col-sm-10
       (submit-button {:class "btn btn-default"} "Sign in")]]]))
