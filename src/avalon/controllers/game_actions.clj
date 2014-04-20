(ns avalon.controllers.game-actions
  (:use avalon.globals ring.util.response)
  (:require [avalon.game-interface :as game-interface]))

(defn join [request id]
  (do
    (game-interface/add-new-player id (:id active-user) 0)
    (redirect (str "/game/" id))))

(defn propose [request id]
  (do
    (game-interface/choose-team id (:id active-user) (:team (:params request))))
    (redirect (str "/game/" id)))
