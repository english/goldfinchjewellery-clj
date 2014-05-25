(ns goldfinchjewellery.models.migration
  (:require [clojure.java.jdbc :as sql]))

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

(def news-seed
  "clojure map of a previously pulled down feed of the live db"
  [{:image_url nil, :content "Goldsmiths’ Craft and Design Council. Craftsmanship and Design Awards 2005. ‘Commended’ for 3D production jewellery.", :category "Awards"}
   {:image_url nil, :content "Goldsmiths’ Council. Craftsmanship and Design Awards 2003. ‘Silver’ for finished pieces.", :category "Awards"}
   {:image_url nil, :content "British Jewellers’ Association, 2002, Certificate of Merit for Higher National Diploma in Jewellery and Silversmithing, 2nd year student – 1st prize.", :category "Awards"}
   {:image_url nil, :content "B.J.A. 2001, Commendation Award for H.N.D. in Jewellery and Silversmithing, 1st year student.", :category "Awards"}
   {:image_url nil, :content "B.J.A. 2001, Awarded the travel bursary.", :category "Awards"}
   {:image_url nil, :content "Arthur Price of England Design Competition 2001, 1st prize for designing a bowl, which was produced and sold by the Arthur Price Company.", :category "Awards"}
   {:image_url "http://goldfinchjewellery.s3-eu-west-1.amazonaws.com/the_guide.jpg", :content "The Guide", :category "Press"}
   {:image_url nil, :content "Heart Gallery. 4 Market St, Hebden Bridge, West Yorkshire, HX7 6AA. \r\nwww.heartgallery.co.uk ", :category "Stockists"}
   {:image_url nil, :content "Red Barn Gallery. Melkinthorpe, Penrith, Cumbria, CA10 2DR. Tel 01931 712 767. \r\ninfo@redbarngallery.co.uk - http://www.redbarngallery.co.uk", :category "Stockists"}
   {:image_url nil, :content "Goldsmiths’ Craft and Design Council. Craftsmanship and Design Awards 2011. ‘Commendation’ for Fashion Design Production.\r\n", :category "Awards"}])

(def jewellery-seed
  "clojure map of a previously pulled down feed to the live db"
  [{:image_url "http://goldfinchjewellery.s3-eu-west-1.amazonaws.com/C15.jpg", :gallery "Weather", :description "Oxidised silver and oval Labradorite cloud pendant.", :name "C15"}
   {:image_url "http://goldfinchjewellery.s3-eu-west-1.amazonaws.com/C29.jpg", :gallery "Weather", :description "Oxidised silver and oval Labradorite drop necklace.", :name "C29"}
   {:image_url "http://goldfinchjewellery.s3-eu-west-1.amazonaws.com/C20.jpg", :gallery "Weather", :description "Oxidised silver and oval Labradorite ring.", :name "C20"}
   {:image_url "http://goldfinchjewellery.s3-eu-west-1.amazonaws.com/C26.jpg", :gallery "Weather", :description "Oxidised silver and Labradorite cloud pendant.", :name "C26"}
   {:image_url "http://goldfinchjewellery.s3-eu-west-1.amazonaws.com/C19-C27.jpg", :gallery "Weather", :description "Oxidised silver and Labradorite cloud pendants.", :name "C19 C27"}
   {:image_url "http://goldfinchjewellery.s3-eu-west-1.amazonaws.com/C28.jpg", :gallery "Weather", :description "Silver, 18ct yellow gold and Citrine cloud pendant.", :name "C28"}
   {:image_url "http://goldfinchjewellery.s3-eu-west-1.amazonaws.com/g5.2.jpg", :gallery "Commissions", :description "Diamond and recycled 18ct yellow gold ring.", :name "G5.2"}
   {:image_url "http://goldfinchjewellery.s3-eu-west-1.amazonaws.com/dragonfly.jpg", :gallery "Commissions", :description "Fine silver, sterling silver and Peridot dragonfly brooch.", :name "Dragonfly"}
   {:image_url "http://goldfinchjewellery.s3-eu-west-1.amazonaws.com/g5.1.jpg", :gallery "Commissions", :description "Cushion shaped green Tourmaline, recycled 18ct yellow gold and sterling silver ring.", :name "G5.1"}
   {:image_url "http://goldfinchjewellery.s3-eu-west-1.amazonaws.com/g4.1.jpg", :gallery "Branches", :description "Sterling silver, wood, resin and suede brooch.", :name "G4.1"}
   {:image_url "http://goldfinchjewellery.s3-eu-west-1.amazonaws.com/g4.2.jpg", :gallery "Branches", :description "Sterling silver, wood and resin brooch.", :name "G4.2"}
   {:image_url "http://goldfinchjewellery.s3-eu-west-1.amazonaws.com/DSCF0182-copy.jpg", :gallery "Woodlands", :description "Silver acorn pendant.", :name "Dscf0182 Copy"}
   {:image_url "http://goldfinchjewellery.s3-eu-west-1.amazonaws.com/DSCF0159-copy.jpg", :gallery "Woodlands", :description "Silver acorn with enamel oak leaf pendant.", :name "Dscf0159 Copy"}
   {:image_url "http://goldfinchjewellery.s3-eu-west-1.amazonaws.com/DSCF0176-copy.jpg", :gallery "Woodlands", :description "Silver and enamel necklace.", :name "Dscf0176 Copy"}
   {:image_url "http://goldfinchjewellery.s3-eu-west-1.amazonaws.com/DSCF0189-copy.jpg", :gallery "Woodlands", :description "Silver studs.", :name "Dscf0189 Copy"}
   {:image_url "http://goldfinchjewellery.s3-eu-west-1.amazonaws.com/rev_bird55.jpg", :gallery "Birds", :description "Round Peridot and fine silver earrings.", :name "Rev Bird55"}
   {:image_url "http://goldfinchjewellery.s3-eu-west-1.amazonaws.com/g1.1.jpg", :gallery "Birds", :description "No description.", :name "G1.1"}
   {:image_url "http://goldfinchjewellery.s3-eu-west-1.amazonaws.com/g1.3.jpg", :gallery "Birds", :description "Sterling silver long necklace.", :name "G1.3"}
   {:image_url "http://goldfinchjewellery.s3-eu-west-1.amazonaws.com/rev_bird33.jpg", :gallery "Birds", :description "Round Peridot, fine silver textured bird pendant on silver chain.", :name "Rev Bird33"}
   {:image_url "http://goldfinchjewellery.s3-eu-west-1.amazonaws.com/rev_bird44.jpg", :gallery "Birds", :description "Oval Peridot, fine silver textured bird pendant on hand platted silk.", :name "Rev Bird44"}
   {:image_url "http://goldfinchjewellery.s3-eu-west-1.amazonaws.com/rev_bird22.jpg", :gallery "Birds", :description "Oval Citrine set in 18ct yellow gold, fine silver embossed bird pendant on 18ct yellow gold chain.", :name "Rev Bird22"}
   {:image_url "http://goldfinchjewellery.s3-eu-west-1.amazonaws.com/g3.3.jpg", :gallery "Peace Doves", :description "Sterling silver ring with paper and resin.", :name "G3.3"}
   {:image_url "http://goldfinchjewellery.s3-eu-west-1.amazonaws.com/g3.1.jpg", :gallery "Peace Doves", :description "Large sterling silver and cultured pearl dove pendant on hand platted linen. Small sterling silver and cultured pearl brooch.", :name "G3.1"}
   {:image_url "http://goldfinchjewellery.s3-eu-west-1.amazonaws.com/g3.2.jpg", :gallery "Peace Doves", :description "Broochs in sterling silver, suede and acrylic.", :name "G3.2"}])

(defn seed
  "seed db with current live news and jewellery"
  []
  (sql/db-do-commands db "truncate news, jewellery")
  (apply sql/insert! db :news news-seed)
  (apply sql/insert! db :jewellery jewellery-seed))

(defn migrate []
  (create-users-table)
  (create-news-table)
  (create-jewellery-table)
  (seed))
