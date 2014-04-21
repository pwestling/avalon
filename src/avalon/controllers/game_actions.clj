(ns avalon.controllers.game-actions
  (:use avalon.globals ring.util.response)
  (:require [avalon.game-interface :as game-interface]))

(defn join [request id]
  (do
    (game-interface/add-new-player id (:id active-user) 0)
    (redirect (str "/game/" id))))

(defn propose [request id]
  (do
    (game-interface/choose-team id (:id active-user) (map #(Integer/parseInt %) (:team (:params request)))))
    (redirect (str "/game/" id)))

(defn vote [request id]
  (do
    (game-interface/vote id (:id active-user) (= "pass" (:vote (:params request))))
    (redirect (str "/game/" id))))

(defn quest [request id]
  (do
    (game-interface/quest id (:id active-user) (= "pass" (:vote (:params request))))
    (redirect (str "/game/" id))))
