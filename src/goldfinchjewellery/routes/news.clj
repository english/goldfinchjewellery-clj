(ns goldfinchjewellery.routes.news
  (:require [compojure.core :refer [DELETE GET POST defroutes]]
            [goldfinchjewellery.models.image :refer [delete-image image-url
                                                     upload-image]]
            [goldfinchjewellery.models.news :as model]
            [goldfinchjewellery.views.news :as view]
            [noir.util.route :refer [def-restricted-routes]]
            [noir.validation :refer [has-value? valid-file?]]
            [ring.util.response :refer [redirect]]))

(defn create [category content image]
  (let [url (when (valid-file? image) (image-url image))]
    (when (valid-file? image) (upload-image image))
    (model/create category content url)
    (redirect "/news")))

(def-restricted-routes restricted-news-routes
  (GET "/news" []
       (view/index (model/all)))
  (GET "/news/new" []
       (view/news-new))
  (POST "/news" [category content image]
        (if (has-value? content)
          (create category content image)
          (view/news-new content "Conten't can't be blank")))
  (DELETE "/news/:id" [id]
          (delete-image (model/get-by-id id))
          (model/delete id)
          (redirect "/news")))

(defroutes unrestricted-news-routes
  (GET "/news.json" []
       (view/index-json (model/all))))
