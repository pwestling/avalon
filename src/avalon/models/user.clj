(ns avalon.models.user
  (:require [avalon.db :as db]))

(defn new-user [user]
  (let
    [user-id (db/new-entry "user" user)
     name (:name user)]
    (db/set (str "user-name-to-id" ":" name) user-id)
    user-id))

(defn exists? [user-id]
  (db/exists? "user" user-id))

(defn name-exists? [name]
  (db/exists? (str "user-name-to-id" ":" name)))
