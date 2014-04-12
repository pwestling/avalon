(ns avalon.db
  (:require [taoensso.carmine :as car :refer (wcar)]))

(def redis-conn {:pool {} :spec {:host "127.0.0.1" :port 6379}})

(defmacro wcar* [& body]
  `(car/wcar redis-conn ~@body))

(defn get [& commands]
  (wcar* (apply car/get commands)))

(defn set [& commands]
  (wcar* (apply car/set commands)))

(defn new-id [resource]
  (wcar* (car/incr (str resource "-" "id"))))

(defn new-entry [resource value]
  (let
    [new-id (new-id resource)]
    (set (str resource ":" new-id) value)
    new-id))

(defn retrieve-game [gameid]
  (wcar* (car/get (str "game" gameid))))

(defn exists? [resource id]
  (do
    (= 1 (wcar* (car/exists (str resource ":" id))))))

(defn get-entry [resource id]
  (wcar* (car/get (str resource ":" id))))
