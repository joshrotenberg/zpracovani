(ns zpracovani.api.objects
  (:use zpracovani.core))

(defmacro def-parse-objects-method
  [name request-method action & [body-keyword]]
  `(def-parse-method ~name ~request-method ~action ~body-keyword))

(def-parse-objects-method create :post "classes/%s" :object)
(def-parse-objects-method retrieve :get "classes/%s/%s")
(def-parse-objects-method update :put "classes/%s/%s" :update)
(def-parse-objects-method query :get "classes/%s")
(def-parse-objects-method delete :delete "classes/%s/%s")