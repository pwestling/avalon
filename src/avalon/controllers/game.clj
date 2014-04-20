(ns avalon.controllers.game
  (:use compojure.core ring.util.response clojure.set)
  (:require [avalon.views.game :as view]
            [avalon.models.game :as game]
            [avalon.game-interface :as game-interface]))

(declare select-team)
(declare vote)
(declare join-game)
(declare closed-game)
(declare open-game)

(defn show [request id]
  (let
    [game-state (game/find-game id)
     stage (game/get-stage game-state) ; let's fix this stage thing
     stage-name (game/get-stage-name stage)
     stage-info (game/get-stage-info stage)]
    (case stage-name
      :open-game (open-game game-state stage-info)
      :select-team (select-team game-state stage-info)
      :vote (vote game-state stage-info)
      (closed-game game-state stage-info))))

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

(defn open-game [game-state stage-info]
  (view/overview game-state true))

(defn closed-game [game-state stage-info]
  (view/overview game-state false))

(defn select-team [game-state stage-info]
  (let
    [team-size 2] ; get this from the state
    (println stage-info)
    (view/select-team game-state team-size)))

(defn vote [game-state stage-info]
  (view/vote game-state))
