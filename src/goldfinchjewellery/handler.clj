(ns goldfinchjewellery.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [goldfinchjewellery.routes.jewellery
             :refer [restricted-jewellery-routes unrestricted-jewellery-routes]]
            [goldfinchjewellery.routes.news
             :refer [restricted-news-routes unrestricted-news-routes]]
            [goldfinchjewellery.routes.session :refer [session-routes]]
            [hiccup.middleware :refer [wrap-base-url]]
            [noir.session :as session]
            [noir.util.middleware :as middleware]
            [ring.middleware.session.cookie :refer [cookie-store]]
            [ring.util.response :refer [redirect]]))

(defn init []
  (println "goldfinchjewellery is starting"))

(defn destroy []
  (println "goldfinchjewellery is shutting down"))

(defn user-access [request]
  (session/get :user_id))

(defroutes app-routes
  (GET "/" [] (redirect "/login"))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (middleware/app-handler
        [restricted-jewellery-routes unrestricted-jewellery-routes
         restricted-news-routes unrestricted-news-routes session-routes
         app-routes]
        :session-options
        {:store (cookie-store) :secure true}
        :access-rules
        [{:redirect "/login" :rules [user-access]}])
      (handler/site)
      (wrap-base-url)))
