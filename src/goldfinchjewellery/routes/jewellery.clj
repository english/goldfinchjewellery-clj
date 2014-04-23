(ns goldfinchjewellery.routes.jewellery
  (:require [compojure.core :refer [DELETE GET POST defroutes]]
            [goldfinchjewellery.models.jewellery :as model]
            [goldfinchjewellery.views.jewellery :as view]
            [noir.response :refer [redirect]]
            [noir.session :as session]
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
       (if (session/get :user_id)
         (view/index (model/all))
         (redirect "/sessions/new")))
  (GET "/jewellery/new" []
       (if (session/get :user_id)
         (view/new-jewellery-item)
         (redirect "/sessions/new")))
  (POST "/jewellery" [name description gallery image_path]
        (if (session/get :user_id)
          (create name description gallery image_path)
          (redirect "/sessions/new")))
  (DELETE "/jewellery/:id" [id]
          (if (session/get :user_id)
            (do
              (model/delete id)
              (redirect "/jewellery"))
            (redirect "/sessions/new"))))
