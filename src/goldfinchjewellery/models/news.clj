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
                      [:content "TEXT NOT NULL"]
                      [:image_path "STRING"])))

(defn all []
  (sql/with-connection
    db (sql/with-query-results res
         ["SELECT * FROM news ORDER BY category, created_at DESC"]
         (doall res))))

(defn get-by-id [id]
  (sql/with-connection
    db
    (sql/with-query-results res
      ["SELECT * FROM news WHERE id = ?" id]
      (first res))))

(defn create [category content & [image_path]]
  (sql/with-connection
    db
    (sql/insert-values
      :news
      [:category :content :created_at :image_path]
      [category content (java.util.Date.) image_path])))

(defn delete [id]
  (sql/with-connection
    db
    (sql/do-prepared "DELETE FROM news WHERE news.id = ?" [id])))
