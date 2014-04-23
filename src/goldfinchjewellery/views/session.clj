(ns goldfinchjewellery.views.session
  (:require [goldfinchjewellery.views.helpers :refer [control]]
            [goldfinchjewellery.views.layout :as layout]
            [hiccup.form :refer [email-field password-field submit-button]]))

(defn sessions-new [& [email password errors]]
  (layout/common
    [:h1 "Log In"]
    (for [error errors]
      [:p.erorr error])
    [:form.form-horizontal {:action "/sessions" :method "POST"}
     (control email-field "email" email {:placeholder "someone@example.com"})
     (control password-field "password" password {:placeholder "a super secret password"})
     [:div.form-group
      [:div.col-sm-offset-2.col-sm-10
       (submit-button {:class "btn btn-default"} "Sign in")]]]))
