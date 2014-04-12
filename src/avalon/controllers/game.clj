(ns avalon.controllers.game
  (:use compojure.core ring.util.response clojure.set)
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
     params (:form-params request)
     attributes { :name (get params "name") :num-players (get params "num-players") :roles (intersection #{"percival" "morgana" "oberon"} (set (keys params))) }]
    (game/new-game params)
    (redirect origin)))
