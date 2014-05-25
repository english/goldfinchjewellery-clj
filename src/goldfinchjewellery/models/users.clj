(ns goldfinchjewellery.models.users
  (:require [clojure.java.jdbc :as sql]
            [goldfinchjewellery.models.migration :refer [db]]
            [noir.util.crypt :as crypt]))

(defn create [email password]
  (sql/insert!
    db :users {:email email :encrypted_password (crypt/encrypt password)}))

(defn find-by-email [email]
  (first (sql/query db ["SELECT * FROM users WHERE email = ?" email])))
