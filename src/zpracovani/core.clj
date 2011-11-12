(ns zpracovani.core
  (:use [clojure.data.json :as json]
        zpracovani.util)
  (:require [clj-http.client :as cclient]))

(def ^:dynamic *client-version* (System/getProperty "zpracovani.version"))
(def ^:dynamic *application-id* nil)
(def ^:dynamic *master-key* nil)

(def ^:dynamic *api-url* "api.parse.com")
(def ^:dynamic *api-version* "1")

(def user-agent (str "zpracovani/" *client-version*))

(defmacro with-credentials
  "Use the Parse API Application ID and Master Key for the contained methods."
  [id key & body]
  `(binding [*application-id* ~id
             *master-key* ~key]
     (do 
       ~@body)))

(defn execute-request [request]
  "Executes the HTTP request and handles the response"
  (let [response (cclient/request request)
        status (:status response)
        body (:body response)
        headers (:headers response)]
    (cond
     (status-is-client-error status) (throw (Exception. "Client error"))
     (status-is-server-error status) (throw (Exception. "Server error"))
     :else
     (when-not (empty? body)
       (json/read-json (:body response))))))

(defn prepare-request [request-method uri first-args arg-map auth-map]
  "Prepares the HTTP request"
  (let [real-uri (apply format uri first-args)
        body (:body arg-map) ;; get the post body
        query-args (dissoc (merge arg-map auth-map) :body)
        auth (vector (get auth-map :application-id)
                     (get auth-map :master-key))]
    {:method request-method
     :url real-uri
     :basic-auth auth
     :query-params query-args
     :content-type :json
     :headers {"User-Agent" user-agent}
     :body body}))

(defmacro def-parse-method
  "Macro to create the Parse API calls"
  [name request-method path & rest]
  `(defn ~name [& args#]
     (let [req-uri# (str "https://" *api-url* "/" *api-version*
                         "/" ~path)
           split-args# (split-with (complement keyword?) args#)
           first-args# (first split-args#)
           arg-map# (transform-args (apply hash-map (second split-args#)))
           auth-map# (merge (when *application-id*
                              {:application-id *application-id*})
                            (when *master-key*
                              {:master-key *master-key*}))
           request# (prepare-request ~request-method
                                     req-uri#
                                     first-args#
                                     arg-map#
                                     auth-map#)]
       (execute-request request#))))

