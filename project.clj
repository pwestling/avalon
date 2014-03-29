(defproject avalon "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies
    [[org.clojure/clojure "1.5.1"]
     [compojure "1.1.6"]
     [com.taoensso/carmine "2.4.6"]]
  :plugins
    [[lein-ring "0.8.10"]]
  :ring {:handler avalon.handler/app}
  :profiles
    {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
