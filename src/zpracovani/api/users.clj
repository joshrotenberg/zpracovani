(ns zpracovani.api.users
  (:use zpracovani.core))

(defmacro def-parse-users-method
  [name request-method action & [body-keyword]]
  `(def-parse-method ~name ~request-method ~action ~body-keyword))

;; signup a user
;;
;;`(signup :user {:username "foo" :password "bar"})`
(def-parse-users-method signup :post "users" :user)

;; login a user
;;
;; `(login {:username "foo" :password "bar"})`
(def-parse-users-method login :get "login")

;; retrieve a user (via the object id)
;;
;; `(retrieve "Zwe2tJWn42")`
(def-parse-users-method retrieve :get "users/%s")

;; update a user
;;
;; `(update "Zwe2tJWn42" :update {:new "stuff"})`
(def-parse-users-method update :put "users/%s" :update)

;; query all users or for a specific user
;;
;; `(query)` or `(query :where {:username "foo"})`
(def-parse-users-method query :get "users")

;; delete a user
;;
;; `(delete "Zwe2tJWn42")`
(def-parse-users-method delete :delete "users/%s")

