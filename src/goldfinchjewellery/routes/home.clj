(ns goldfinchjewellery.routes.home
  (:require [compojure.core :refer :all]
            [ring.util.response :refer :all]
            [hiccup.form :refer :all]
            [goldfinchjewellery.views.layout :as layout]
            [goldfinchjewellery.models.news :as news]))

(defn sessions-new [& [email password error]]
  (layout/common
    [:h1 "Log In "]
    (when error [:p.error error])
    [:form.form-horizontal {:action "/sessions" :method "POST"}
     [:div.form-group
      [:label.col-sm-2.control-label {:for "email"} "Email"]
      [:div.col-sm-10
       [:input#email.form-control {:name "email" :placeholder "Enter email" :type "text"} email]]]
     [:div.form-group
      [:label.col-sm-2.control-label {:for "password"} "Password"]
      [:div.col-sm-10
       [:input#password.form-control {:name "password" :placeholder "Password" :type "password"} password]]]
     [:div.form-group
      [:div.col-sm-offset-2.col-sm-10
       [:input.btn.btn-default {:name "commit" :type "submit" :value "Sign in"}]]]]))

(defn sessions-create [email password]
  (cond
    (empty? email)
    (sessions-new email password "Some dummy forgot to leave an email")
    (empty? password)
    (sessions-new email password "Don't you have a password?")
    :else
    (redirect "/news")))

(defn news-index []
  (layout/common
    [:a.btn.btn-primary {:href "/news/new"} "New News Item"]
    [:table.table
     [:thead [:th "Content"] [:th "Category"] [:th]]
     [:tbody
      (for [news-item (news/all)]
        [:tr
         [:td [:p.content (:content news-item)]]
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

(defroutes home-routes
  (GET "/" [] (redirect "/sessions/new"))
  (GET "/sessions/new" [] (sessions-new))
  (POST "/sessions" [email password]
        (sessions-create email password))
  (GET "/news" [] (news-index))
  (GET "/news/new" [] (news-new))
  (POST "/news" [category content]
        (news/create category content)
        (redirect "/news"))
  (DELETE "/news/:id" [id]
          (news/delete id)
          (redirect "/news")))
