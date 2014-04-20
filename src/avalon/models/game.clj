(ns avalon.models.game
  (:require [avalon.db :as db]
            [avalon.models.user :as user]
            [avalon.core :as avalon]))

(defn all []
  (db/get-all "game"))

(defn find-game [id]
  (db/get-entry "game" id))

(defn get-stage [game]
  (avalon/current-phase game))

(defn players [game]
  (map user/get-user (:players game)))

(defn new-game [attributes]
  (db/new-entry "game" attributes))

(defn delete-game [id]
  (db/delete-entry "game" id))
