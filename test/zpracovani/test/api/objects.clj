(ns zpracovani.test.api.objects
  (:use zpracovani.core
        zpracovani.api.objects
        zpracovani.test.properties
        clojure.data
        clojure.test))

(deftest objects-test
  (with-credentials *parse-application-id* *parse-master-key*
    (let [releases (read-string (slurp "resources/vermiform.txt"))
          results (doall (map #(create *zpracovani-test-class* :object  %) releases))]
      (is (= 17 (count (:results (query *zpracovani-test-class*)))))
      (is (= 17 (:count (query *zpracovani-test-class* :limit 0 :count 1))))
      (doseq [r results]

        (is (= (:objectId r)
               (:objectId (retrieve *zpracovani-test-class* (:objectId r)))))
        (is (= true (contains? (update *zpracovani-test-class* (:objectId r)
                                       :update {:label "VMFM"})
                               :updatedAt)))
        (is (= "VMFM" (:label (retrieve *zpracovani-test-class* (:objectId r)))))

        )
      (is (= 5 (-> (query *zpracovani-test-class*
                          :where {:artist "Born Against"})
                   :results
                   count)))
      (is (= 4 (-> (query *zpracovani-test-class*
                          :where {:year {:$gt 1990}
                                  :pressings {:$gt 2}})
                              :results
                              count)))
      (is (= '(13 12 11 10 9 8 7 6.5 6 5 4.5 4 3 2 1 0.5 0)
             (map #(:release %) (:results (query *zpracovani-test-class*
                                                 :order "-release")))))
      (doseq [r (:results (query *zpracovani-test-class*))]
        (is (= {} (delete *zpracovani-test-class* (:objectId r))))
        )
      )))



