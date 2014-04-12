(ns avalon.controllers.game
  (:use compojure.core ring.util.response)
  (:require [avalon.views.game :as view]
            [avalon.models.game :as game]))

(defn show [request id]
  (let
    [game-state (game/find-game id)]
    (view/show game-state)))

(defn newg [request] (view/newg))

(defn create [request]
  (let
    [origin (get (:headers request) "origin")
     params (assoc (:form-params request) :stage :start)]
    (game/new-game params)
    (redirect origin)))
