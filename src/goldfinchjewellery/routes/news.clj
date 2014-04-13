(ns goldfinchjewellery.routes.news
  (:require [compojure.core :refer [DELETE GET POST defroutes]]
            [goldfinchjewellery.models.news :as model]
            [goldfinchjewellery.views.news :as view]
            [ring.util.response :refer [redirect]]))

(defroutes news-routes
  (GET "/news" [] (view/index model/all))
  (GET "/news/new" [] (view/news-new))
  (POST "/news" [category content]
        (model/create category content)
        (redirect "/news"))
  (DELETE "/news/:id" [id]
          (model/delete id)
          (redirect "/news")))
