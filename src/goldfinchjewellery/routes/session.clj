(ns goldfinchjewellery.routes.session
  (:require [compojure.core :refer [GET POST defroutes]]
            [goldfinchjewellery.models.users :as users]
            [goldfinchjewellery.views.session :as view]
            [noir.session :as session]
            [noir.util.crypt :as crypt]
            [noir.validation :refer [errors? get-errors has-value? is-email?  min-length? rule]]
            [ring.util.response :refer [redirect]]))

(defn authenticate [email password]
  (let [user (users/find-by-email email)]
    (and (not (nil? user))
         (crypt/compare password (:encrypted_password user)))))

(defn create [email password]
  (rule (is-email? email)
        [:email "Valid email required"])
  (rule (and (has-value? password)
             (min-length? password 6))
        [:password "Password must be at least 6 characters"])
  (rule (authenticate email password)
        [:mismatch "Wrong username or password"])
  (if (errors?)
    (view/sessions-new email password (get-errors))
    (do
      (session/put! :logged-in true)
      (redirect "/news"))))

(defroutes session-routes
  (GET "/login" []
       (if (session/get :logged-in)
         (redirect "/news")
         (view/sessions-new)))
  (GET "/logout" []
       (session/remove! :logged-in)
       (redirect "/login"))
  (POST "/sessions" [email password] (create email password)))
