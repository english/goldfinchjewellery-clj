(ns goldfinchjewellery.views.layout
  (:require [hiccup.page :refer [html5 include-css]]))

(defn common [& content]
  (html5
    [:head
     [:title "Welcome to goldfinchjewellery"]
     [:link {:rel "stylesheet" :href "//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css"}]
     (include-css "/css/screen.css")]
    [:body
     [:div.container
      [:nav.navbar.navbar-default {:role "navigation"}
       [:div.conainer-fluid
        [:div.navbar-header
         [:span.navbar-brand "Admin"]]
        [:div.collapse.navbar-collapse
         [:ul.nav.navbar-nav
          [:li [:a {:href "/news"} "Manage News"]]
          [:li [:a {:href "/jewellery"} "Manage Jewellery"]]]]]]
      content]]))
