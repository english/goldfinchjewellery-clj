(ns goldfinchjewellery.views.jewellery
  (:require [clojure.data.json :as json]
            [goldfinchjewellery.models.jewellery :as model]
            [goldfinchjewellery.views.helpers :refer [control]]
            [goldfinchjewellery.views.layout :as layout]
            [hiccup.element :refer [link-to]]
            [hiccup.form :refer [drop-down file-upload form-to hidden-field
                                 label submit-button text-area text-field]]
            [markdown.core :refer [md-to-html-string]]))

(defn index-json [jewellery]
  {:status 200
   :body (json/write-str
           (->> jewellery
                (map #(assoc % :html (md-to-html-string (:description %))))
                (map #(select-keys % [:name :gallery :html :image_path]))))
   :headers {"Content-Type" "application/json"}})

(defn index [jewellery]
  (layout/common
    (link-to {:class "btn btn-primary"} "/jewellery/new" "New Jewellery Item")
    [:table.table
     [:thead [:th "Name"] [:th "Description"] [:th "Gallery"] [:th "Image Path"]]
     [:tbody
      (for [jewellery-item jewellery]
        [:tr
         [:td (:name jewellery-item)]
         [:td (md-to-html-string (:description jewellery-item))]
         [:td [:p.gallery (:gallery jewellery-item)]]
         [:td [:img {:src (:image_path jewellery-item)}]]
         [:td (form-to {:class "form"} [:post (str "/jewellery/" (:id jewellery-item))]
                       (hidden-field "_method" "DELETE")
                       (submit-button {:class "btn btn-danger"} "Delete"))]])]]))

(defn new-jewellery-item [& [name description gallery image_path errors]]
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
