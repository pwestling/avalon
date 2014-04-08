(ns avalon.db
  (:require [taoensso.carmine :as car :refer (wcar)]))

(def redis-conn {:pool {} :spec {:host "127.0.0.1" :port 6379}})

(defmacro wcar* [& body]
  `(car/wcar redis-conn ~@body))

(defn get [& commands]
  (wcar* (apply car/get commands)))

(defn set [& commands]
  (wcar* (apply car/set commands)))

(defn retrieve-game [gameid]
  (wcar* (car/get (str "game" gameid))))
