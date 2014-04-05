(ns avalon.controllers.game
  (:use compojure.core ring.util.response)
  (:require [avalon.views.game :as view]))

(defn logged-in []
  (let [] false))

(defn show [game-name]
  (if (not (logged-in))
    (redirect "/login")
    (str "Welcome to game: " game-name "!")))

(defn newg [] (view/newg))

(defn create [request]
  (let
    [origin (get (:headers request) "origin")]
    (redirect origin)))
