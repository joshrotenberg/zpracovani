(ns zpracovani.test.example.dsl
  (:use zpracovani.core
        zpracovani.test.properties
        clojure.test)
  (:require [zpracovani.api.users :as zu]
            [zpracovani.api.objects :as zo])
  (:import org.joda.time.DateTime))


;; generic query container function that merges all of our elements together
;; into a single query map 
(defn query
  [& criteria]
  (apply merge criteria))

(defmacro defn-method
  [name class arg]
  `(condp = ~arg
     
     :query
     (defn ~name [& criteria#]
       (with-credentials *parse-application-id* *parse-master-key*
         (zo/query ~class :where (apply query criteria#))))
     
     :create
     (defn ~name [object#]
       (with-credentials *parse-application-id* *parse-master-key*
         (zo/create ~class :object object#)))

     :update
     (defn ~name [id# update#]
       (with-credentials *parse-application-id* *parse-master-key*
         (zo/update ~class id# :update)))

     :delete
     (defn ~name [id#]
       (with-credentials *parse-application-id* *parse-master-key*
         (zo/delete ~class id#)
         ))))

;; define our dsl for this specific class of items
(defn-method find-tasks "ToDoTasks" :query)
(defn-method create-task "ToDoTasks" :create)

;; and some helpers to wrap up common queries
(defn task-name
  [task]
  {:task task})  

(defn created-before
  [date]
  {:createdAt {:$lt {:__type "Date" :iso date}}})

(defn created-after
  [date]
  {:createdAt {:$gt {:__type "Date" :iso date}}})

;; get some variation in the created times
(defn create-task-slowly
  [task]
  (do
    (Thread/sleep 1000)
    (create-task (task-name task))))

(def task-names ["buy farm" "buy alpaca chow" "buy alpaca" "feed alpaca"])

(deftest "dsl"
  (let [results (map create-task-slowly task-names)]
    
    (is (= 3 (count (-> (find-tasks (created-after
                                     (:createdAt (first results))))
                        :results))))
    ))



