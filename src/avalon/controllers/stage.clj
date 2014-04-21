(ns avalon.controllers.stage
  (:use avalon.globals)
  (:require [avalon.views.stage :as stage]
            [avalon.models.user :as user]
            [avalon.core :as core]))

(defn open-game [game-state]
  (stage/overview game-state true))

(defn propose-team [game-state]
  (let
    [team-size 2
     leader (user/get-user (core/current-leader game-state))] ; get this from the state
    (if (core/valid-to-propose-team? game-state (:id active-user))
      (stage/select-team game-state team-size)
      (stage/watch-team-selection game-state leader team-size))))

(defn vote [game-state]
  (let
    [info (core/current-team-selection game-state)
     team (map user/get-user (:proposed-team info))
     leader (user/get-user (:leader info))]
    (if (core/valid-to-vote-for-team? game-state (:id active-user))
      (stage/vote (:id game-state) leader team)
      (stage/waiting-for-votes leader team))))

(defn mission [game-state]
  (let
    [info (core/current-mission game-state)
     team (map user/get-user (:team info))]
    (if (core/valid-to-vote-for-mission? game-state (:id active-user))
      (stage/mission (:id game-state) team)
      (stage/watch-mission team))))

(defn merlin-guess [game-state]
  (let
    [good-players (map user/get-user (core/good-players game-state))
     evil-players (map user/get-user (core/evil-players game-state))]
    (if (core/is-good? game-state (:id active-user))
      (stage/watch-merlin-guess evil-players)
      (stage/merlin-guess (:id game-state) good-players))))

(defn closed-game [game-state]
  (stage/overview game-state false))
