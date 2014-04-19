(ns avalon.models.user
  (:require [avalon.db :as db]))

(defn get-user [id]
  (db/get-entry "user" id))

(defn new-user [user]
  (let
    [user-id (db/new-entry "user" user)
     name (:name user)]
    (db/db-set (str "user-name-to-id" ":" name) user-id)
    user-id))

(defn exists? [user-id]
  (db/exists? "user" user-id))

(defn name-exists? [name]
  (db/exists? (str "user-name-to-id" ":" name)))
