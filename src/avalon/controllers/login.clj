(ns avalon.controllers.login
  (:use compojure.core ring.util.response)
  (:require [avalon.views.login :as view]))

(defn update-name [name])

(defn index []
  (view/index))

(defn create [name request]
  (let
    [origin (get (:headers request) "origin")]
    (update-name name)
    (redirect origin)))
