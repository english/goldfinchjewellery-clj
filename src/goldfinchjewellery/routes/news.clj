(ns goldfinchjewellery.routes.news
  (:require [compojure.core :refer [DELETE GET POST defroutes]]
            [goldfinchjewellery.models.news :as model]
            [goldfinchjewellery.views.news :as view]
            [noir.util.route :refer [def-restricted-routes]]
            [noir.validation :refer [errors? get-errors has-value? rule]]
            [ring.util.response :refer [redirect]]))

(defn create [category content]
  (rule (has-value? content)
        [:content "Content can't be blank"])
  (if (errors?)
    (view/news-new content (get-errors))
    (do (model/create category content)
        (redirect "/news"))))

(def-restricted-routes restricted-news-routes
  (GET "/news" []
       (view/index (model/all)))
  (GET "/news/new" []
       (view/news-new))
  (POST "/news" [category content]
        (create category content))
  (DELETE "/news/:id" [id]
          (model/delete id)
          (redirect "/news")))

(defroutes unrestricted-news-routes
  (GET "/news.json" []
       (view/index-json (model/all))))
