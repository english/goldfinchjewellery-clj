(ns goldfinchjewellery.views.jewellery
  (:require [goldfinchjewellery.models.jewellery :as model]
            [goldfinchjewellery.views.helpers :refer [control]]
            [goldfinchjewellery.views.layout :as layout]
            [hiccup.form :refer [drop-down form-to hidden-field submit-button
                                 text-area text-field]]
            [markdown.core :refer [md-to-html-string]]))

(defn index [jewellery]
  (layout/common
    [:a.btn.btn-primary {:href "/jewellery/new"} "New Jewellery Item"]
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
    (form-to {:class "form form-horizontal"} [:post "/jewellery"]
             (if errors
               [:div.col-sm-offset-2.col-sm-10 [:p.text-danger errors]])
             (control text-field "name" name)
             (control text-area "description" description {:rows "5"})
             (control drop-down "gallery" model/galleries)
             (control text-field "image_path" image_path)
             [:div.form-group
              [:div.col-sm-offset-2.col-sm-10
               (submit-button {:class "btn btn-primary"} "Save")]])))
