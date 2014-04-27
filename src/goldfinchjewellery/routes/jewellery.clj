(ns goldfinchjewellery.routes.jewellery
  (:require [aws.sdk.s3 :as aws]
            [clojure.string :as s]
            [compojure.core :refer [DELETE GET POST defroutes]]
            [goldfinchjewellery.models.jewellery :as model]
            [goldfinchjewellery.views.jewellery :as view]
            [noir.response :refer [redirect]]
            [noir.util.route :refer [def-restricted-routes]]
            [noir.validation :refer [errors? get-errors has-values? rule]]))

(def cred {:access-key (System/getenv "AWS_ACCESS_KEY_ID")
           :secret-key (System/getenv "AWS_SECRET_ACCESS_KEY")
           :endpoint "s3-eu-west-1.amazonaws.com"})

(def bucket "goldfinchjewellery")

(defn image-url [filename]
  (str "http://" bucket "." (:endpoint cred) "/" filename))

(defn s3-key-for-image [url]
  (let [path (.getPath (java.net.URI. url))]
    (s/replace-first path "/" "")))

(defn delete-jewellery-image [jewellery]
  (aws/delete-object cred bucket (s3-key-for-image (:image_path jewellery))))

(defn create [name description gallery image]
  (rule (has-values? [name description gallery image])
        [:all "all fields are required"])
  (if (errors?)
    (view/new-jewellery-item name description gallery image (get-errors))
    (do
      (aws/put-object cred "goldfinchjewellery"
                      (:filename image) (:tempfile image))
      (model/create name description gallery (image-url (:filename image)))
      (redirect "/jewellery"))))

(def-restricted-routes restricted-jewellery-routes
  (GET "/jewellery" []
       (view/index (model/all)))
  (GET "/jewellery/new" []
       (view/new-jewellery-item))
  (POST "/jewellery" [name description gallery image]
        (create name description gallery image))
  (DELETE "/jewellery/:id" [id]
          (delete-jewellery-image (model/get-by-id id))
          (model/delete id)
          (redirect "/jewellery")))

(defroutes unrestricted-jewellery-routes
  (GET "/jewellery.json" [] (view/index-json (model/all))))
