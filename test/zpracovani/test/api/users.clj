(ns zpracovani.test.api.users
  (:use zpracovani.core
        zpracovani.util
        zpracovani.api.users
        zpracovani.test.properties
        re-rand
        clojure.data
        clojure.test))

(deftest users-test
  (with-credentials *parse-application-id* *parse-master-key*
    (let [user {:username (re-rand #"[A-Za-z0-9]{10}")
                :password (re-rand #"[A-Za-z0-9]{10}")}
          new-user (signup :user user)
          logged-in (login :username (:username user)
                           :password (:password user))]
      (is (= true (contains? new-user :objectId)))
      (is (= (:username user) (:username logged-in)))
      (is (= logged-in (retrieve (:objectId new-user))))
      (is (contains?
           (update (:objectId new-user) :body {:rad "things"})
           :updatedAt))
      (is (= "things" (:rad (retrieve (:objectId new-user)))))
      (is (= (retrieve (:objectId new-user))
             (-> (query)
                 :results
                 first)))
      (is (= (retrieve (:objectId new-user))
             (-> (query :where
                        {:username (:username user)})
                       :results
                       first)))
      (is (= {}  (delete (:objectId new-user)))))))

