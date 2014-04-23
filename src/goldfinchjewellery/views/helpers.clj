(ns goldfinchjewellery.views.helpers
  (:require [clojure.string :as string]
            [hiccup.form :refer [label]]))

(defn control
  ([field name & [value attrs]]
   (let [capitalized (string/capitalize (string/replace name "_" " "))]
     [:div.form-group
      (label {:class "col-sm-2 control-label"} name capitalized)
      [:div.col-sm-10
       (field (assoc attrs :class "form-control") name value)]])))
