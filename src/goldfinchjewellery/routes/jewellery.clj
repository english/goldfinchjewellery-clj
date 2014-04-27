(ns goldfinchjewellery.routes.jewellery
  (:require [compojure.core :refer [DELETE GET POST defroutes]]
            [goldfinchjewellery.models.jewellery :as model]
            [goldfinchjewellery.views.jewellery :as view]
            [noir.response :refer [redirect]]
            [noir.util.route :refer [def-restricted-routes]]
            [noir.validation :refer [errors? get-errors has-values? rule]]))

(defn create [name description gallery image-path]
  (rule (has-values? [name description gallery image-path])
        [:all "all stuff is required"])
  (if (errors? :all)
    (view/new-jewellery-item name description gallery image-path (get-errors))
    (do
      (model/create name description gallery image-path)
      (redirect "/jewellery"))))

(def-restricted-routes restricted-jewellery-routes
  (GET "/jewellery" []
       (view/index (model/all)))
  (GET "/jewellery/new" []
       (view/new-jewellery-item))
  (POST "/jewellery" [name description gallery image_path]
        (create name description gallery image_path))
  (DELETE "/jewellery/:id" [id]
          (model/delete id)
          (redirect "/jewellery")))

(defroutes unrestricted-jewellery-routes
  (GET "/jewellery.json" []
       (view/index-json (model/all))))
