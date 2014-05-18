(ns goldfinchjewellery.views.jewellery
  (:require [clojure.data.json :as json]
            [goldfinchjewellery.models.jewellery :as model]
            [goldfinchjewellery.views.helpers :refer [control]]
            [goldfinchjewellery.views.layout :as layout]
            [hiccup.element :refer [link-to]]
            [hiccup.form :refer [drop-down file-upload form-to hidden-field
                                 label submit-button text-area text-field]]
            [markdown.core :refer [md-to-html-string]]
            [ring.util.response :refer [response]]))

(defn index-json [jewellery]
  (->> jewellery
       (map #(assoc % :html (md-to-html-string (:description %))))
       (map #(assoc % :image_path (:image_url %)))
       (map #(select-keys % [:name :description :gallery :html :image_path]))
       (assoc {} :jewellery)
       (response)))

(defn index [jewellery]
  (layout/common
    (link-to {:class "btn btn-primary"} "/jewellery/new" "New Jewellery Item")
    [:table.table
     [:thead [:th "Gallery"] [:th "Description"] [:th "Image"]]
     [:tbody
      (for [jewellery-item jewellery]
        [:tr
         [:td [:p.gallery (:gallery jewellery-item)]]
         [:td (md-to-html-string (:description jewellery-item))]
         [:td [:img {:src (:image_url jewellery-item)}]]
         [:td (form-to {:class "form"} [:post (str "/jewellery/" (:id jewellery-item))]
                       (hidden-field "_method" "DELETE")
                       (submit-button {:class "btn btn-danger"} "Delete"))]])]]))

(defn new-jewellery-item [& [name description gallery image-url errors]]
  (layout/common
    [:h1 "New Jewellery Item"]
    (form-to {:enctype "multipart/form-data" :class "form form-horizontal"}
             [:post "/jewellery"]
             (if errors
               [:div.col-sm-offset-2.col-sm-10 [:p.text-danger errors]])
             (control text-field "name" name)
             (control text-area "description" description {:rows "5"})
             (control drop-down "gallery" model/galleries)
             [:div.form-group
              (label {:class "col-sm-2 control-label"} "image" "Image")
              [:div.col-sm-10 (file-upload "image")]]
             [:div.form-group
              [:div.col-sm-offset-2.col-sm-10
               (submit-button {:class "btn btn-primary"} "Save")]])))
