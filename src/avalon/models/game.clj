(ns avalon.models.game
  (:require [avalon.db :as db]))

(def GAME-PROPOSAL
  {:name "LiveRamp"
   :stage :propose
   :players [["Takashi" 2] ["Chris" 3] ["Porter" 4] ["Jacob" 1]]})

(def GAME-VOTE
  {:name "LiveRamp"
   :stage :vote
   :team [["Takashi" 2] ["Chris" 3]]})

(defn all []
  (db/get-all "game"))

(defn find-game [id]
  (db/get-entry "game" id))

(defn new-game [attributes]
  (db/new-entry "game" attributes))
