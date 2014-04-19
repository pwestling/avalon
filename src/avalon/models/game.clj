(ns avalon.models.game
  (:require [avalon.db :as db]
            [avalon.models.user :as user]))

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

(defn get-stage [game]
  (or (last (:rounds game)) :open-game))

(defn players [game]
  (map user/get-user (:players game)))

(defn new-game [attributes]
  (db/new-entry "game" attributes))

(defn delete-game [id]
  (db/delete-entry "game" id))
