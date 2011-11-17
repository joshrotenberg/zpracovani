(ns zpracovani.test.example.dsl.helpers
  (:use zpracovani.core
        zpracovani.test.properties)
  (:require [zpracovani.api.objects :as zo]))


;; generic query container function that merges all of our elements together
;; into a single query map 
(defn query
  [& criteria]
  (apply merge criteria))

;; this macro creates our class specific query function. this might make more
;; sense if you had a few classes and wanted a separate "find" for each without
;; having to create each on individually. see below.
(defmacro defn-method
  [name class]
  `(defn ~name [& criteria#]
     (with-credentials *parse-application-id* *parse-master-key*
       (zo/query ~class :where (apply query criteria#)))))

;; these helpers wrap up common queries against our record collection. 
(defn album-title
  [title]
  {:title title})  

(defn artist-name
  [name]
  {:artist name})

(defn created-after
  [date]
  {:createdAt {:$gt {:__type "Date" :iso date}}})

(defn year-released
  [year]
  {:year year})

(defn number-pressings
  [number]
  {:pressings number})

(defn number-released
  [number]
  {:number number})

(defn is-greater-than
  [arg]
  {:$gt arg})

(defn is-less-than
  [arg]
  {:$lt arg})

(defn in-the-range
  [low high]
  {:$gte low, :$lte high})

(defn release-number
  [release]
  {:release release})

(defn album-format
  [format]
  {:format format})
