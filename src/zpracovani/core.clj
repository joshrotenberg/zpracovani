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

(defn execute-request 
  "Executes the HTTP request and handles the response"
  [request]
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

(defn prepare-request 
  "Prepares the HTTP request"
  [request-method request-uri first-args query-params where body auth]
  (let [real-uri (apply format request-uri first-args)]
    {:method request-method
     :url real-uri
     :basic-auth auth
     :query-params (if-not (empty? where)
                     (assoc query-params :where (json/json-str where))
                     query-params)
     :content-type :json
     :headers {"User-Agent" user-agent}
     :body (if-not (empty? body)
             (json/json-str body))}))

(defmacro def-parse-method
  "Macro to create the Parse API calls"
  [name request-method path & [body-keyword]]
  `(defn ~name [& args#]
     (let [request-uri# (str "https://" *api-url* "/" *api-version* "/" ~path)
           split-args# (split-positional-args args#) 
           first-args# (first split-args#)
           next-args# (second split-args#)
           auth# (vector *application-id* *master-key*)
           body# (get next-args# ~body-keyword)
           where# (:where next-args#)
           query-params# (dissoc next-args# :where ~body-keyword)
           request# (prepare-request ~request-method
                                     request-uri#
                                     first-args#
                                     query-params#
                                     where#
                                     body#
                                     auth#)]
       (execute-request request#))))

