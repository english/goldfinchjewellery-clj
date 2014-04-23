(ns goldfinchjewellery.routes.session
  (:require [compojure.core :refer [GET POST defroutes]]
            [goldfinchjewellery.models.users :as users]
            [goldfinchjewellery.views.session :as view]
            [noir.session :as session]
            [noir.util.crypt :as crypt]
            [ring.util.response :refer [redirect]]))

(defn authenticate [email password]
  (let [user (users/find-by-email email)]
    (and (not (nil? user))
         (crypt/compare password (:encrypted_password user)))))

(defn create [email password]
  (cond
    (empty? email)
    (view/sessions-new email password "Some dummy forgot to leave an email")
    (empty? password)
    (view/sessions-new email password "Don't you have a password?")
    (authenticate email password)
    (do
      (session/put! :user_id (:id (users/find-by-email email)))
      (redirect "/news"))
    :else
    (view/sessions-new email password "Wrong username or password")))

(defroutes session-routes
  (GET "/sessions/new" []
       (if (session/get :user_id)
         (redirect "/news")
         (view/sessions-new)))
  (POST "/sessions" [email password] (create email password)))
