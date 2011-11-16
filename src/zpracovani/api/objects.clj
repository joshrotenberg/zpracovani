(ns zpracovani.api.objects
  (:use zpracovani.core))

(defmacro def-parse-objects-method
  [name request-method action & [body-keyword]]
  `(def-parse-method ~name ~request-method ~action ~body-keyword))

;; create an object
;;
;;`(create "MyObjectType" :object {:got "stuff" :here "yo"})`
(def-parse-objects-method create :post "classes/%s" :object)

;; retrieve an object
;;
;;`(retrieve "MyObjectType" "Zwe2tJWn42")`
(def-parse-objects-method retrieve :get "classes/%s/%s")

;; update an object
;;
;;`(update "MyObjectType" "Zwe2tJWn42" :update {:new "stuff"})`
(def-parse-objects-method update :put "classes/%s/%s" :update)

;; query for an object
;;
;;`(query "MyObjectType" :where {:new "stuff"})`
(def-parse-objects-method query :get "classes/%s")

;; delete an object
;;
;;`(delete "MyObjectType" "Zwe2tJWn42")`
(def-parse-objects-method delete :delete "classes/%s/%s")