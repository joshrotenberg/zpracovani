(ns zpracovani.util)

(defn split-positional-args
  [args]
  (let [split-args (split-with (complement keyword?) args)]
    (conj (list (apply hash-map (second split-args)))
          (first split-args))
  ))

(defn status-is-server-error
  [status]
  "Return true if the HTTP status denotes a server error"
  (some #{status} (range 500 510)))

(defn status-is-client-error
  [status]
  "Return true if the HTTP status denotes a client error"
  (some #{status} (range 400 426)))
