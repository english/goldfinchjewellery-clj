(ns goldfinchjewellery.routes.jewellery
  (:require [compojure.core :refer [DELETE GET POST defroutes]]
            [goldfinchjewellery.models.image :refer [delete-image image-url upload-image]]
            [goldfinchjewellery.models.jewellery :as model]
            [goldfinchjewellery.views.jewellery :as view]
            [noir.response :refer [redirect]]
            [noir.util.route :refer [def-restricted-routes]]
            [noir.validation :refer [errors? get-errors has-values? rule valid-file?]]))

(defn create [name description gallery image]
  (rule (has-values? [name description gallery image]) [:all "all fields are required"])
  (rule (valid-file? image) [:image "all fields are required"])
  (if (errors?)
    (view/new-jewellery-item name description gallery image (get-errors))
    (do
      (upload-image image)
      (model/create name description gallery (image-url image))
      (redirect "/jewellery"))))

(def-restricted-routes restricted-jewellery-routes
  (GET "/jewellery" []
       (view/index (model/all)))
  (GET "/jewellery/new" []
       (view/new-jewellery-item))
  (POST "/jewellery" [name description gallery image]
        (create name description gallery image))
  (DELETE "/jewellery/:id" [id]
          (delete-image (model/get-image-url-by-id id))
          (model/delete id)
          (redirect "/jewellery")))

(defroutes unrestricted-jewellery-routes
  (GET "/jewellery.json" [] (view/index-json (model/all))))
