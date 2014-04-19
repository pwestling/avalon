(ns avalon.controllers.game
  (:use compojure.core ring.util.response clojure.set)
  (:require [avalon.views.game :as view]
            [avalon.models.game :as game]
            [avalon.game-interface :as game-interface]))

(declare propose)
(declare vote)
(declare join-game)
(declare closed-game)

(defn show [request id]
  (let
    [game-state (game/find-game id)
     stage (game/get-stage game-state)]
    (println stage)
    (println game-state)
    (case stage
      :open-game (open-game game-state)
      :propose (propose game-state)
      :vote (vote game-state)
      (closed-game game-state))))

(defn newg [request] (view/newg))

(defn join-game [request game-id]
  (let
    [user-id (:id (:current-user (:params request)))
    resp (game-interface/add-new-player game-id user-id 0)]
    (redirect (str "/game/" game-id))))

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

(defn open-game [game-state]
  (view/overview game-state true))

(defn closed-game [game-state]
  (view/overview game-state false))

(defn propose [game-state]
  (view/propose game-state))

(defn vote [game-state]
  (view/vote game-state))
