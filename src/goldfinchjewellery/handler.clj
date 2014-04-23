(ns goldfinchjewellery.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [goldfinchjewellery.routes.jewellery :refer [jewellery-routes]]
            [goldfinchjewellery.routes.news :refer [news-routes]]
            [goldfinchjewellery.routes.session :refer [session-routes]]
            [hiccup.middleware :refer [wrap-base-url]]
            [noir.util.middleware :as middleware]
            [ring.middleware.session.cookie :refer [cookie-store]]
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
  (-> (middleware/app-handler
        [jewellery-routes news-routes session-routes app-routes]
        :session-options
        {:store (cookie-store) :secure true})
      (handler/site)
      (wrap-base-url)))
