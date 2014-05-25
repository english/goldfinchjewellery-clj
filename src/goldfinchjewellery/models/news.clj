(ns goldfinchjewellery.models.news
  (:require [clojure.java.jdbc.deprecated :as sql]
            [goldfinchjewellery.models.migration :refer [db]]))

(def categories ["Stockists" "Events & Exhibitions" "Awards" "Press"])

(defn all []
  (sql/with-connection db
    (sql/with-query-results res
      ["SELECT * FROM news ORDER BY category, created_at DESC"]
      (doall res))))

(defn get-image-url-by-id [id]
  (sql/with-connection db
    (sql/with-query-results res
      ["SELECT image_url FROM news WHERE id = ?" id]
      (:image_url (first res)))))

(defn create [category content & [image-url]]
  (sql/with-connection db
    (sql/insert-values
      :news
      [:category :content :created_at :image_url]
      [category content (java.util.Date.) image-url])))

(defn delete [id]
  (sql/with-connection db
    (sql/do-prepared "DELETE FROM news WHERE news.id = ?" [id])))
