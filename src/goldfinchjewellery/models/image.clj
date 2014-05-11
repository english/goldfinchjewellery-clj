(ns goldfinchjewellery.models.image
  (:require [aws.sdk.s3 :as aws]
            [clojure.string :as s]))

(def cred {:access-key (System/getenv "AWS_ACCESS_KEY_ID")
           :secret-key (System/getenv "AWS_SECRET_ACCESS_KEY")
           :endpoint "s3-eu-west-1.amazonaws.com"})

(def bucket "goldfinchjewellery")

(defn image-url [image]
  "builds an S3 url given an uploaded image"
  (str "http://" bucket "." (:endpoint cred) "/" (:filename image)))

(defn s3-key-for-image [url]
  "given an S3 url, returns the key to use for querying S3 with"
  (-> (java.net.URI. url)
      (.getPath)
      (s/replace-first "/" "")))

(defn delete-image [url]
  (let [s3-key (s3-key-for-image url)]
    (aws/delete-object cred bucket s3-key)))

(defn upload-image [image]
  (let [key (:filename image)
        value (:tempfile image)]
    (aws/put-object cred "goldfinchjewellery" key value)))
