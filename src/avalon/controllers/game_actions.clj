(ns avalon.controllers.game-actions
  (:use avalon.globals ring.util.response)
  (:require [avalon.game-interface :as game-interface]))

(defn join [request game-id]
  (let
    [user-id (:id active-user)
    resp (game-interface/add-new-player game-id user-id 0)]
    (redirect (str "/game/" game-id))))

(defn propose [request id]
  (do
    (game-interface/choose-team id (:id active-user) (:team (:params request))))
    (redirect (str "/game/" id)))
