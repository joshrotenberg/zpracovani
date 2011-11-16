(ns zpracovani.api.users
  (:use zpracovani.core))

(defmacro def-parse-users-method
  [name request-method action & [body-keyword]]
  `(def-parse-method ~name ~request-method ~action ~body-keyword))

(def-parse-users-method signup :post "users" :body)
(def-parse-users-method login :get "login")
(def-parse-users-method retrieve :get "users/%s")
(def-parse-users-method update :put "users/%s" :body)
(def-parse-users-method query :get "users")
(def-parse-users-method delete :delete "users/%s")

