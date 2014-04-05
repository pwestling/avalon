(ns avalon.controllers.game
  (:use compojure.core ring.util.response)
  (:require [avalon.views.game :as view]))

(defn show [game-name]
  (str "Welcome to game: " game-name "!"))

(defn newg [] (view/newg))

(defn create [request]
  (let
    [origin (get (:headers request) "origin")]
    (redirect origin)))
