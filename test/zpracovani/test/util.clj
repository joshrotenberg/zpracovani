(ns zpracovani.test.util
  (:use zpracovani.util
        clojure.test))

(deftest split-positional-args-test
  ;; test the first string args
  (is (= '("foo" "bar")
         (first (split-positional-args '("foo" "bar" :what "now")))))
  ;; test the second keyword args
  (is (= "baz" (:dos (-> '("uno" :dos "baz")
                         split-positional-args
                         second))))
  )