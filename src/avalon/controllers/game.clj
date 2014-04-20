(ns avalon.controllers.game
  (:use compojure.core ring.util.response clojure.set avalon.globals)
  (:require [avalon.views.game :as view]
            [avalon.models.game :as game]
            [avalon.models.user :as user]
            [avalon.game-interface :as game-interface]
            [avalon.core :as core]))

(declare propose-team)
(declare vote)
(declare mission)
(declare merlin-guess)
(declare closed-game)
(declare open-game)

(defn show [request id]
  (let
    [game-state (game/find-game id)
     stage (game/get-stage game-state)
     stage-info {}]
    (println stage)
    (case stage
      :open-game (open-game game-state stage-info)
      :propose-team (propose-team game-state)
      :team-vote (vote game-state stage-info)
      :mission (mission game-state)
      :merlin-guess (merlin-guess game-state)
      (closed-game game-state stage-info))))

(defn newg [request] (view/newg))

(defn join-game [request game-id]
  (let
    [user-id (:id active-user)
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

(defn propose [request id]
  (do
    (game-interface/choose-team id (:id active-user) (:team (:params request))))
    (redirect (str "/game/" id)))

; game views
(defn mission [game-state])
(defn merlin-guess [game-state])

(defn open-game [game-state stage-info]
  (view/overview game-state true))

(defn closed-game [game-state stage-info]
  (view/overview game-state false))

(defn propose-team [game-state]
  (let
    [team-size 2
     leader (user/get-user (core/current-leader game-state))] ; get this from the state
    (if (core/valid-to-propose-team? game-state (:id active-user))
      (view/select-team game-state team-size)
      (view/watch-team-selection game-state leader team-size))))

(defn vote [game-state stage-info]
  (view/vote game-state))
