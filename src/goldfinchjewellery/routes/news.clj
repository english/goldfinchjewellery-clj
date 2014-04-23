(ns goldfinchjewellery.routes.news
  (:require [compojure.core :refer [DELETE GET POST defroutes]]
            [goldfinchjewellery.models.news :as model]
            [goldfinchjewellery.views.news :as view]
            [noir.session :as session]
            [ring.util.response :refer [redirect]]))

(defroutes news-routes
  (GET "/news" []
       (if (session/get :user_id)
         (view/index (model/all))
         (redirect "/sessions/new")))
  (GET "/news.json" [] (view/index-json (model/all)))
  (GET "/news/new" []
       (if (session/get :user_id)
         (view/news-new)
         (redirect "/sessions/new")))
  (POST "/news" [category content]
       (if (session/get :user_id)
         (do (model/create category content)
             (redirect "/news"))
         (redirect "/sessions/new")))
  (DELETE "/news/:id" [id]
       (if (session/get :user_id)
         (do (model/delete id)
             (redirect "/news"))
         (redirect "/sessions/new"))))
