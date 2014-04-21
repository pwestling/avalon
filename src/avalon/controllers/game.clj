(ns avalon.controllers.game
  (:use compojure.core ring.util.response clojure.set avalon.globals)
  (:require [avalon.views.game :as view]
            [avalon.controllers.stage :as stage]
            [avalon.models.game :as game]
            [avalon.models.user :as user]
            [avalon.game-interface :as game-interface]
            [avalon.core :as core]))

(defn show [request id]
  (let
    [game-state (game/find-game id)
     stage (game/get-stage game-state)]
    (println stage)
    (case stage
      :open         (stage/open-game game-state)
      :propose-team (stage/propose-team game-state)
      :team-vote    (stage/vote game-state)
      :mission      (stage/mission game-state)
      :merlin-guess (stage/merlin-guess game-state)
                    (stage/closed-game game-state))))

(defn newg [request] (view/newg))

(defn create [request]
  (let
    [origin (get (:headers request) "origin")
     params (:form-params request)
     attributes { :name (get params "name") :num-players (Integer/parseInt (get params "num-players")) :roles (intersection #{"percival" "morgana" "oberon"} (set (keys params))) }]
    (if (game-interface/valid-game? attributes)
      (redirect (str "/game/" (game-interface/new-game attributes)))
      (view/newg)))) ;display error

(defn delete [request id]
  (let []
    (game/delete-game id)
    (redirect "/")))
