(ns goldfinchjewellery.models.news
  (:require [clojure.java.jdbc :as sql]))

(def db {:classname "org.sqlite.JDBC"
         :subprotocol "sqlite"
         :subname "db.sq3"})

(def categories
  ["Stockists" "Events & Exhibitions" "Awards" "Press"])

(defn create-news-table []
  (sql/with-connection
    db
    (sql/create-table :news
                      [:id "INTEGER PRIMARY KEY AUTOINCREMENT"]
                      [:created_at "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"]
                      [:category "TEXT NOT NULL"]
                      [:content "TEXT NOT NULL"])))

(defn all []
  (sql/with-connection
    db (sql/with-query-results res
         ["SELECT * FROM news ORDER BY category, created_at DESC"]
         (doall res))))

(defn create [category content]
  (sql/with-connection
    db
    (sql/insert-values
      :news
      [:category :content :created_at]
      [category content (java.util.Date.)])))

(defn delete [id]
  (sql/with-connection
    db
    (sql/do-prepared "DELETE FROM news WHERE news.id = ?" [id])))
