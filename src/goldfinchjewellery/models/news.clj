(ns goldfinchjewellery.models.news
  (:require [clojure.java.jdbc :as sql])
  (:import java.sql.DriverManager))

(def db {:classname "org.sqlite.JDBC",
         :subprotocol "sqlite",
         :subname "db.sq3"})

(defn create-table []
  (sql/with-connection
    db
    (sql/create-table :news
                      [:id "INTEGER PRIMARY KEY AUTOINCREMENT"]
                      [:created_at "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"]
                      [:category "TEXT"]
                      [:content "TEXT"])))

(defn all []
  (sql/with-connection
    db
    (sql/with-query-results res
      ["SELECT * FROM news ORDER BY created_at DESC"] (doall res))))

(defn create [category content]
  (sql/with-connection
    db
    (sql/insert-values
      :news
      [:category :content :created_at]
      [category content (new java.util.Date)])))

(defn delete [id]
  (sql/with-connection
    db
    (sql/do-prepared "DELETE FROM news WHERE news.id = ?" [id])))
