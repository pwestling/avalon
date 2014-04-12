(ns avalon.db
  (:require [taoensso.carmine :as car :refer (wcar)]))

(def redis-conn {:pool {} :spec {:host "127.0.0.1" :port 6379}})

(defmacro wcar* [& body]
  `(car/wcar redis-conn ~@body))

(defn db-get [& commands]
  (wcar* (apply car/get commands)))

(defn get-all [resource]
  (let
    [keys (wcar* (car/keys (str resource ":*")))]
    (wcar* (apply car/mget keys))))

(defn db-set [& commands]
  (wcar* (apply car/set commands)))

(defn new-id [resource]
  (wcar* (car/incr (str resource "-" "id"))))

(defn new-entry [resource value]
  (let
    [new-id (new-id resource)
     value-with-id (assoc value "id" new-id)]
    (db-set (str resource ":" new-id) value-with-id)
    new-id))

(defn get-entry [resource id]
  (wcar* (car/get (str resource ":" id))))

(defn exists? [resource id]
  (do
    (= 1 (wcar* (car/exists (str resource ":" id))))))

(defn get-entry [resource id]
  (wcar* (car/get (str resource ":" id))))

<<<<<<< HEAD
(defn delete-entry [resource id]
  (wcar* (car/del (str resource ":" id))))
=======
(defn set-entry [resource id value]
  (wcar* (car/set (str resource ":" id) value)))
>>>>>>> minor changes
