(ns goldfinchjewellery.routes.session
  (:require [compojure.core :refer [GET POST defroutes]]
            [goldfinchjewellery.views.session :as view]
            [ring.util.response :refer [redirect]]))

(defn create [email password]
  (cond
    (empty? email)
    (view/sessions-new email password "Some dummy forgot to leave an email")
    (empty? password)
    (view/sessions-new email password "Don't you have a password?")
    (and (= password "secret")
         (= email "someone@example.com"))
    (redirect "/news")
    :else
    (view/sessions-new email password "Wrong username or password")))

(defroutes session-routes
  (GET "/sessions/new" [] (view/sessions-new))
  (POST "/sessions" [email password] (create email password)))
