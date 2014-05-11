(ns goldfinchjewellery.views.news
  (:require [clojure.data.json :as json]
            [goldfinchjewellery.models.news :as model]
            [goldfinchjewellery.views.helpers :refer [control]]
            [goldfinchjewellery.views.layout :as layout]
            [hiccup.element :refer [image link-to]]
            [hiccup.form :refer [drop-down file-upload form-to hidden-field
                                 label submit-button text-area]]
            [markdown.core :refer [md-to-html-string]]))

(defn index [news]
  (layout/common
    (link-to {:class "btn btn-primary"} "/news/new" "New News Item")
    [:table.table
     [:thead [:th "Content"] [:th "Category"] [:th]]
     [:tbody
      (for [news-item news]
        [:tr
         [:td (md-to-html-string (:content news-item))
          (if (:image_url news-item)
              [:div (image (:image_url news-item))])]
         [:td [:p.category (:category news-item)]]
         [:td (form-to {:class "form"} [:post (str "/news/" (:id news-item))]
                       (hidden-field "_method" "DELETE")
                       (submit-button {:class "btn btn-danger"} "Delete"))]])]]))

(defn index-json [news]
  {:status 200
   :body (json/write-str
           (->> news
                (map #(assoc % :html (md-to-html-string (:content %))))
                (map #(select-keys % [:category :html :created_at]))))
   :headers {"Content-Type" "application/json"}})

(defn news-new [& [content errors]]
  (layout/common
    [:h1 "New News Item"]
    (form-to {:enctype "multipart/form-data" :class "form form-horizontal"}
             [:post "/news"]
             (if errors
               [:div.col-sm-offset-2.col-sm-10 [:p.text-danger errors]])
             (control drop-down "category" model/categories)
             (control text-area "content" content {:rows "10"})
             [:div.form-group
              (label {:class "col-sm-2 control-label"} "image" "Image")
              [:div.col-sm-10 (file-upload "image")]]
             [:div.form-group
              [:div.col-sm-offset-2.col-sm-10
               (submit-button {:class "btn btn-primary"} "Save")]])))
