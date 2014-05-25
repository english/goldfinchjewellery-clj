(ns goldfinchjewellery.models.migration
  (:require [clojure.java.jdbc :as sql]
            [clj-http.client :as http]
            [clojure.data.json :as json]
            [clojure.walk :as walk]))

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
                      updated_at timestamp default current_timestamp not null,
                      name varchar(255) not null,
                      gallery varchar(255) not null,
                      description text not null,
                      image_url varchar(255) not null
                    )"]))

(def news-dump "[{\"id\":8,\"content\":\"Goldsmiths’ Craft and Design Council. Craftsmanship and Design Awards 2005. ‘Commended’ for 3D production jewellery.\",\"category\":\"Awards\",\"created_at\":\"2013-03-09T19:15:31.382Z\",\"updated_at\":\"2013-03-09T19:15:31.382Z\",\"image_path\":null},{\"id\":9,\"content\":\"Goldsmiths’ Council. Craftsmanship and Design Awards 2003. ‘Silver’ for finished pieces.\",\"category\":\"Awards\",\"created_at\":\"2013-03-09T19:15:31.426Z\",\"updated_at\":\"2013-03-09T19:15:31.426Z\",\"image_path\":null},{\"id\":10,\"content\":\"British Jewellers’ Association, 2002, Certificate of Merit for Higher National Diploma in Jewellery and Silversmithing, 2nd year student – 1st prize.\",\"category\":\"Awards\",\"created_at\":\"2013-03-09T19:15:31.453Z\",\"updated_at\":\"2013-03-09T19:15:31.453Z\",\"image_path\":null},{\"id\":11,\"content\":\"B.J.A. 2001, Commendation Award for H.N.D. in Jewellery and Silversmithing, 1st year student.\",\"category\":\"Awards\",\"created_at\":\"2013-03-09T19:15:31.470Z\",\"updated_at\":\"2013-03-09T19:15:31.470Z\",\"image_path\":null},{\"id\":12,\"content\":\"B.J.A. 2001, Awarded the travel bursary.\",\"category\":\"Awards\",\"created_at\":\"2013-03-09T19:15:31.487Z\",\"updated_at\":\"2013-03-09T19:15:31.487Z\",\"image_path\":null},{\"id\":13,\"content\":\"Arthur Price of England Design Competition 2001, 1st prize for designing a bowl, which was produced and sold by the Arthur Price Company.\",\"category\":\"Awards\",\"created_at\":\"2013-03-09T19:15:31.512Z\",\"updated_at\":\"2013-03-09T19:15:31.512Z\",\"image_path\":null},{\"id\":14,\"content\":\"The Guide\",\"category\":\"Press\",\"created_at\":\"2013-03-09T20:20:19.665Z\",\"updated_at\":\"2013-03-09T20:20:19.665Z\",\"image_path\":\"http://goldfinchjewellery.s3-eu-west-1.amazonaws.com/the_guide.jpg\"},{\"id\":29,\"content\":\"Heart Gallery. 4 Market St, Hebden Bridge, West Yorkshire, HX7 6AA. \\r\\nwww.heartgallery.co.uk \",\"category\":\"Stockists\",\"created_at\":\"2014-03-12T16:53:34.019Z\",\"updated_at\":\"2014-03-12T16:53:34.019Z\",\"image_path\":null},{\"id\":30,\"content\":\"Red Barn Gallery. Melkinthorpe, Penrith, Cumbria, CA10 2DR. Tel 01931 712 767. \\r\\ninfo@redbarngallery.co.uk - http://www.redbarngallery.co.uk\",\"category\":\"Stockists\",\"created_at\":\"2014-03-12T16:56:33.178Z\",\"updated_at\":\"2014-03-12T16:56:33.178Z\",\"image_path\":null},{\"id\":31,\"content\":\"Goldsmiths’ Craft and Design Council. Craftsmanship and Design Awards 2011. ‘Commendation’ for Fashion Design Production.\\r\\n\",\"category\":\"Awards\",\"created_at\":\"2014-03-12T17:02:06.176Z\",\"updated_at\":\"2014-03-12T17:02:06.176Z\",\"image_path\":null}]")

(defn get-news []
  (->> news-dump
       (json/read-str)
       (map #(walk/keywordize-keys %))
       (map #(assoc % :image_url (:image_path %)))
       (sort-by :id)
       (map #(select-keys % [:category :content :image_url]))))

(defn get-jewellery []
  (->> (http/get "http://goldfinchjewellery.herokuapp.com/jewellery.json" {:as :json})
       (:body)
       (:jewellery)
       (map #(assoc % :image_url (:image_path %)))
       (sort-by :created_at)
       (map #(select-keys % [:name :description :gallery :image_url]))))

(defn seed
  "seed db with current live news and jewellery"
  []
  (sql/db-do-commands db "truncate news, jewellery")
  (apply sql/insert! db :news (get-news))
  (apply sql/insert! db :jewellery (get-jewellery)))

(defn migrate []
  (create-users-table)
  (create-news-table)
  (create-jewellery-table)
  (seed))
