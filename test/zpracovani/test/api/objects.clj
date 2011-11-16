(ns zpracovani.test.api.objects
  (:use zpracovani.core
        zpracovani.api.objects
        zpracovani.test.properties
        clojure.data
        clojure.test))

(deftest objects-test
  (with-credentials *parse-application-id* *parse-master-key*
    (let [releases (read-string (slurp "resources/vermiform.txt"))
          results (doall (map #(create "RecordCollection" :object  %) releases))]
      (is (= 7 (count (:results (query "RecordCollection")))))
      (is (= 7 (:count (query "RecordCollection" :limit 0 :count 1))))
      (doseq [r results]

        (is (= (:objectId r)
               (:objectId (retrieve "RecordCollection" (:objectId r)))))
        (is (= true (contains? (update "RecordCollection" (:objectId r)
                                       :update {:label "VMFM"})
                               :updatedAt)))
        (is (= "VMFM" (:label (retrieve "RecordCollection" (:objectId r)))))

        )
      (is (= 2 (-> (query "RecordCollection"
                          :where {:artist "Born Against"})
                   :results
                   count)))
      (is (= "Lifesblood" (-> (query "RecordCollection"
                                     :where {:year {:$gt 1990}
                                             :pressings {:$gt 2}})
                              :results
                              first
                              :artist)))
      (is (= '(4.5 4 3 2 1 0.5 0)
             (map #(:release %) (:results (query "RecordCollection"
                                                 :order "-release")))))
      (doseq [r (:results (query "RecordCollection"))]
        (is (= {} (delete "RecordCollection" (:objectId r))))
        )
      
      )))



