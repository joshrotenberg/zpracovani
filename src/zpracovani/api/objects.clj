(ns zpracovani.api.objects
  (:use zpracovani.core))

(defmacro def-parse-objects-method
  [name request-method action & rest]
  `(def-parse-method ~name ~request-method ~action ~@rest))

(def-parse-objects-method create :post "classes/%s")
(def-parse-objects-method retrieve :get "classes/%s/%s")
(def-parse-objects-method update :put "classes/%s/%s")
(def-parse-objects-method query :get "classes/%s")
(def-parse-objects-method delete :delete "classes/%s/%s")