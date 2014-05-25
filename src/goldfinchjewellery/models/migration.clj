(ns goldfinchjewellery.models.migration
  (:require [clojure.java.jdbc :as sql]
            [clj-http.client :as http]))

(def db (or (System/getenv "DATABASE_URL")
              "postgresql://localhost:5432/goldfinchjewellery"))

(defn create-users-table []
  (sql/execute! db ["create table users (
                      email varchar(255) primary key,
                      encrypted_password varchar(255) not null,
                      created_at timestamp default current_timestamp not null
                    )"]))

(defn create-news-table []
  (sql/execute! db ["create table news (
                      id serial primary key,
                      created_at timestamp default current_timestamp not null,
                      category varchar(255) not null,
                      content text not null,
                      image_url varchar(255)
                    )"]))

(defn create-jewellery-table []
  (sql/execute! db ["create table jewellery (
                      id serial primary key,
                      created_at timestamp default current_timestamp not null,
                      name varchar(255) not null,
                      gallery varchar(255) not null,
                      description text not null,
                      image_url varchar(255) not null
                    )"]))

(defn seed
  "seed db with current live news and jewellery"
  []
  (sql/insert! db :news (get-news))
  (sql/insert! db :jewellery (get-jewellery)))

(defn migrate []
  (create-users-table)
  (create-news-table)
  (create-jewellery-table)
  (seed))
