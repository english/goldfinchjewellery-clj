(ns goldfinchjewellery.routes.news
  (:require [compojure.core :refer [DELETE GET POST defroutes]]
            [goldfinchjewellery.models.news :as model]
            [goldfinchjewellery.views.news :as view]
            [noir.util.route :refer [restricted]]
            [ring.util.response :refer [redirect]]))

(defroutes news-routes
  (GET "/news" []
       (restricted
         (view/index (model/all))))
  (GET "/news.json" []
       (view/index-json (model/all)))
  (GET "/news/new" []
       (restricted
         (view/news-new)))
  (POST "/news" [category content]
        (restricted
          (model/create category content)
          (redirect "/news")))
  (DELETE "/news/:id" [id]
          (restricted
            (model/delete id)
            (redirect "/news"))))
