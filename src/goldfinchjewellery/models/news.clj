(ns goldfinchjewellery.models.news
  (:require [clojure.java.jdbc :as sql :refer [do-prepared insert-values
                                               with-connection]]))

(def db {:classname "org.sqlite.JDBC",
         :subprotocol "sqlite",
         :subname "db.sq3"})

(defn create-table []
  (with-connection
    db
    (create-table :news
                      [:id "INTEGER PRIMARY KEY AUTOINCREMENT"]
                      [:created_at "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"]
                      [:category "TEXT"]
                      [:content "TEXT"])))

(defn all []
  (with-connection
    db (sql/with-query-results res
         ["SELECT * FROM news ORDER BY category, created_at DESC"]
         (doall res))))

(defn create [category content]
  (with-connection
    db
    (insert-values
      :news
      [:category :content :created_at]
      [category content (java.util.Date.)])))

(defn delete [id]
  (with-connection
    db
    (do-prepared "DELETE FROM news WHERE news.id = ?" [id])))
