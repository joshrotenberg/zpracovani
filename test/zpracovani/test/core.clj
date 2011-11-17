(ns zpracovani.test.core
  (:use zpracovani.core
        zpracovani.test.properties
        clojure.test)
  (:require [zpracovani.api.objects :as zo]))

;; test data helpers
(defn create-test-data
  [data class]
  (with-credentials *parse-application-id* *parse-master-key*
    (doall (map #(zo/create class :object %) data))))

(defn delete-test-data
  [class]
  (with-credentials *parse-application-id* *parse-master-key*
    (doseq [r (:results (zo/query class))]
      (zo/delete class (:objectId r)))))

(defmacro with-test-data
  "Create test data, run the body, and then delete said data"
  [data class & body]
  `(do
     (create-test-data ~data ~class)
     ~@body
     (delete-test-data ~class)))