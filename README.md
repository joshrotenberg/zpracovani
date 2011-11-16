# zpracování

A Clojure client for the [Parse](http://parse.com) REST API.

## Usage

Familiarize yourself with the Parse REST API
[docs](https://www.parse.com/docs/rest) and create an account/application.

In your project.clj:

```
[zpracovani "0.0.1-SNAPSHOT"]
```

```clojure
(ns my.app
    (:use zpracovani.core
    	  [zpracovani.api users objects]
     ....
))

(def my-app-id "<your application id>")
(def my-master-key "<your master key>")
(with-credentials my-app-id my-master-key
    ;; signup a user
    (signup :body (json-str {:username "jan" :password "secret"}))
    ;; store an object
    (create "ToDo" :body (json-str {:date "tomorrow" :task "feed alpacas"}))
)
```

## Testing

Specify an application ID and a Master Key in the
resources/test.properties file to run the unit tests.

## Examples

See the [unit tests](https://github.com/joshrotenberg/zpracovani/tree/master/test/zpracovani/test/api).

## Status

All user and object calls should be supported.

## Name

[zpracování](http://en.wiktionary.org/wiki/zpracov%C3%A1n%C3%AD)

Czech

processing (action)

## License

Copyright (C) 2011 Josh Rotenberg

Distributed under the Eclipse Public License, the same as Clojure.
