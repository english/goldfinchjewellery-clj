(ns goldfinchjewellery.views.news
  (:require [goldfinchjewellery.views.layout :as layout]
            [hiccup.form :refer [drop-down form-to hidden-field label
                                 submit-button text-area]]
            [markdown.core :refer [md-to-html-string]]))

(defn index [news]
  (layout/common
    [:a.btn.btn-primary {:href "/news/new"} "New News Item"]
    [:table.table
     [:thead [:th "Content"] [:th "Category"] [:th]]
     [:tbody
      (for [news-item (news)]
        [:tr
         [:td (md-to-html-string (:content news-item))]
         [:td [:p.category (:category news-item)]]
         [:td (form-to {:class "form"} [:post (str "/news/" (:id news-item))]
                       (hidden-field "_method" "DELETE")
                       (submit-button {:class "btn btn-danger"} "Delete"))]])]]))

(defn news-new []
  (layout/common
    [:h1 "New News Item"]
    (form-to {:class "form form-horizontal"} [:post "/news"]
             [:div.form-group
              (label {:class "col-sm-2 control-label"} "category" "Category")
              [:div.col-sm-10
               (drop-down {:class "form-control"} "category"
                          ["Stockists" "Events & Exhibitions" "Awards" "Press"])]]
             [:div.form-group
              (label {:class "col-sm-2 control-label"} "content" "Content")
              [:div.col-sm-10
               (text-area {:class "form-control" :rows "10"} "content")]]
             [:div.form-group
              [:div.col-sm-offset-2.col-sm-10
               (submit-button {:class "btn btn-primary"} "Save")]])))
