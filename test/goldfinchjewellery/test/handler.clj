(ns goldfinchjewellery.test.handler
  (:require [clojure.data.json :as json]
            [clojure.java.jdbc :as sql]
            [clojure.test :refer :all]
            [goldfinchjewellery.handler :refer :all]
            [goldfinchjewellery.models.migration :refer [db]]
            [ring.mock.request :refer :all]))

(def test-db "postgresql://localhost:5432/goldfinchjewellery-test")

(defn seed []
  (sql/db-do-commands db "truncate news, jewellery")
  (sql/insert! test-db :news
               {:image_url "http://example.com/image.jpg"
                :category "Press"
                :content "Some *test* content."}
               {:image_url nil
                :category "Stockists"
                :content "# Heading \r Some other **stuff**."})
  (sql/insert! test-db :jewellery
               {:image_url "http://example.com/cloud-necklace.jpg"
                :gallery "Weather"
                :description "Oxidised silver and oval Labradorite drop necklace, that looks *really* like a cloud!"
                :name "A cloud"}
               {:image_url "http://example.com/dove.jpg"
                :gallery "Peace Doves"
                :description "What a peaceful dove."
                :name "A Peace Dove"}))

(defn set-test-db [f]
  (binding [goldfinchjewellery.models.migration/db "postgresql://localhost:5432/goldfinchjewellery-test"]
    (seed)
    (f)))

(use-fixtures :each set-test-db)

(deftest test-app
  (testing "news route"
    (let [response (app (request :get "/news.json"))
          json (-> response (:body) (json/read-str :key-fn keyword))]
      (is (= (:status response)
             200))
      (is (= (count (:news json))
             2))
      (is (= (-> json (:news) (first) (:html))
             "<p>Some <em>test</em> content.</p><div><img src=\"http://example.com/image.jpg\" /></div>"))
      (is (= (get-in response [:headers "Access-Control-Allow-Origin"])
             "*"))))

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= (:status response) 404)))))
