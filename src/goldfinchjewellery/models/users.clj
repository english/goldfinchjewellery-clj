(ns goldfinchjewellery.models.users
  (:require [clojure.java.jdbc :as sql]
            [noir.util.crypt :as crypt]))

(def db {:classname "org.sqlite.JDBC"
         :subprotocol "sqlite"
         :subname "db.sq3"})

(defn create-users-table []
  (sql/with-connection db
    (sql/create-table :users
                  [:id "INTEGER PRIMARY KEY AUTOINCREMENT"]
                  [:created_at "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"]
                  [:email "TEXT UNIQUE"]
                  [:encrypted_password "TEXT"])))

(defn create [email password]
  (sql/with-connection db
    (sql/insert-record
      :users
      {:email email
       :encrypted_password (crypt/encrypt password)})))

(defn find-by-email [email]
  (sql/with-connection db
    (sql/with-query-results res
      ["SELECT * FROM users WHERE email = ?" email]
      (first res))))
