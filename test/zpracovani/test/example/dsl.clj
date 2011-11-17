(ns zpracovani.test.example.dsl
  (:use zpracovani.core
        zpracovani.test.core
        zpracovani.test.properties
        zpracovani.test.example.dsl.helpers
        clojure.test)
  (:require [zpracovani.api.users :as zu]
            [zpracovani.api.objects :as zo]))

;; define our record collection specific query function
(defn-method find-albums *zpracovani-test-class*)

;; now we have all of the pieces we need to test out our dsl. populate
;; the collection.
(let [albums (read-string (slurp "resources/vermiform.txt"))]
  
  (deftest record-collection-dsl
    (with-test-data albums *zpracovani-test-class*
      ;; find the name of the artist for the album
      (is (= "Born Against"
             ( -> (find-albums (album-title "Eulogy"))
                  :results
                  first
                  :artist)))
      
      ;; find out how many albums had more than two pressings
      (is (= 5 (-> (find-albums (number-pressings (is-greater-than 2)))
                   :results
                   count)))
      
      ;; find out how many albums were released in 1993
      (is (= 3 (-> (find-albums (year-released 1993))
                   :results
                   count)))
      
      ;; ferret out a release with a more complex query
      (let [results (find-albums (year-released 1991)
                                 (number-released (is-greater-than 3000))
                                 (number-pressings (in-the-range 1 4))
                                 (album-format "LP"))
            a (first (:results results))]
        (is (= 1991 (:year a)))
        (is (> (:number a) 3000))
        (is (>= (:pressings a) 1))
        (is (<= (:pressings a) 4))
        (is (= "LP" (:format a)))
        )
      
      ;; find out the total number of pressings for everything
      (is (= 38 (reduce + (map :pressings
                               (-> (find-albums)
                                   :results)))))
      )))


