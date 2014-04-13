(ns goldfinchjewellery.handler
  (:require [compojure.core :refer [GET defroutes routes]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [goldfinchjewellery.routes.news :refer [news-routes]]
            [goldfinchjewellery.routes.session :refer [session-routes]]
            [hiccup.middleware :refer [wrap-base-url]]
            [ring.util.response :refer [redirect]]))

(defn init []
  (println "goldfinchjewellery is starting"))

(defn destroy []
  (println "goldfinchjewellery is shutting down"))

(defroutes app-routes
  (GET "/" [] (redirect "/sessions/new"))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (routes news-routes session-routes app-routes)
      (handler/site)
      (wrap-base-url)))
