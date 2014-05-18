(ns goldfinchjewellery.views.news
  (:require [clj-time.coerce :refer [from-long]]
            [clj-time.format :refer [formatters unparse]]
            [goldfinchjewellery.models.news :as model]
            [goldfinchjewellery.views.helpers :refer [control]]
            [goldfinchjewellery.views.layout :as layout]
            [hiccup.core :refer [html]]
            [hiccup.element :refer [image link-to]]
            [hiccup.form :refer [drop-down file-upload form-to hidden-field
                                 label submit-button text-area]]
            [markdown.core :refer [md-to-html-string]]
            [ring.util.response :refer [response]]))

(defn news-body [news-item]
  (html
    (md-to-html-string (:content news-item))
    (when-let [url (:image_url news-item)] [:div (image url)])))

(defn index [news]
  (layout/common
    (link-to {:class "btn btn-primary"} "/news/new" "New News Item")
    [:table.table
     [:thead [:th "Category"] [:th "Content"] [:th]]
     [:tbody
      (for [news-item news]
        [:tr
         [:td [:p.category (:category news-item)]]
         [:td (news-body news-item)]
         [:td (form-to {:class "form"} [:post (str "/news/" (:id news-item))]
                       (hidden-field "_method" "DELETE")
                       (submit-button {:class "btn btn-danger"} "Delete"))]])]]))

(defn iso8601 [time]
  (let [date-time (from-long time)
        formatter (formatters :date-time)]
  (unparse formatter date-time)))

(defn index-json [news]
  (->> news
       (map #(assoc % :html (news-body %)))
       (map #(assoc % :createdAt (iso8601 (:created_at %))))
       (map #(assoc % :updatedAt (:createdAt %)))
       (map #(select-keys % [:category :html :createdAt :updatedAt]))
       (assoc {} :news)
       (response)))

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
