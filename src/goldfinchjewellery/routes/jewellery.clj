(ns goldfinchjewellery.routes.jewellery
  (:require [compojure.core :refer [DELETE GET POST defroutes]]
            [goldfinchjewellery.models.jewellery :as model]
            [goldfinchjewellery.views.jewellery :as view]
            [noir.response :refer [redirect]]
            [noir.session :as session]
            [noir.util.route :refer [restricted]]
            [noir.validation :refer [errors? get-errors has-values? rule]]))

(defn create [name description gallery image-path]
  (rule (has-values? [name description gallery image-path])
        [:all "all stuff is required"])
  (if (errors? :all)
    (view/new-jewellery-item name description gallery image-path (get-errors))
    (do
      (model/create name description gallery image-path)
      (redirect "/jewellery"))))

(defroutes jewellery-routes
  (GET "/jewellery" []
       (restricted
         (view/index (model/all))))
  (GET "/jewellery/new" []
       (restricted
         (view/new-jewellery-item)))
  (POST "/jewellery" [name description gallery image_path]
        (restricted
          (create name description gallery image_path)))
  (DELETE "/jewellery/:id" [id]
          (restricted
            (model/delete id)
            (redirect "/jewellery"))))
