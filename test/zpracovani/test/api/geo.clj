(ns zpracovani.test.api.geo
  (:use zpracovani.core
        zpracovani.api.objects
        zpracovani.test.properties
        clojure.data
        clojure.test))

(deftest geo-points
  (with-credentials *parse-application-id* *parse-master-key*
    (let [parks (read-string (slurp "resources/parks.txt"))]

      (doseq [p parks]
        (create "BerkeleyParks" :object {:name (:name p)
                                         :location {:__type "GeoPoint"
                                                    :latitude (:latitude p)
                                                    :longitude (:longitude p)}}))

      ;; we are at Mortar Rock Park. find the rest of the parks in the hills,
      ;; starting with the closest first
      (is (= '("Indian Rock" "John Hinkel Park" "Grotto Rock Park"
               "Great Stone Face" "Cragmont Park" "Dorothy Bolte Park"
               "Remillard Park" "Crescent Park")
             (map :name (:results (query "BerkeleyParks"
                                         :where {:location
                                                 {:$nearSphere
                                                  {:__type "GeoPoint"
                                                   :latitude 37.893347
                                                   :longitude -122.272389}}})))))

      (doseq [r (:results (query "BerkeleyParks"))]
        (is (= {} (delete "BerkeleyParks" (:objectId r))))
        )
      )))

