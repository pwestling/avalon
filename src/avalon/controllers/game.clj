(ns avalon.controllers.game
  (:use compojure.core ring.util.response)
  (:require [avalon.views.game :as view]
            [avalon.models.game :as game]))

(defn show [game-name]
  (let
    [game-state (game/find-game game-name)]
    (view/show game-state)))

(defn newg [] (view/newg))

(defn create [request]
  (let
    [origin (get (:headers request) "origin")]
    (redirect origin)))