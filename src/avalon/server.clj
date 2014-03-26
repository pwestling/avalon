(ns avalon.server
  (:use ring.adapter.jetty))

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Hello World!"})

(defn boot [port]
  (run-jetty handler {:port port}))
