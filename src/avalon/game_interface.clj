(ns avalon.game-interface (:require [taoensso.carmine :as car :refer (wcar)]
                                    [avalon.db :as db]))
(use 'avalon.core)


(defn retrieve-game [gameid] (db/get-entry "game" gameid))
(defn save-game [state] (db/set-entry "game" (:id state) state))
(defn make-new-game [state] (db/new-entry "game" state))

(defn noop  [state] state)

(defn perform-validated-action-on-gamestate [validator action transition gameid playerid actionInfo]
  (let [gamestate (retrieve-game gameid)]
    (if (and (in-game gamestate playerid)
             (validator gamestate playerid))
      (-> gamestate
          (action playerid actionInfo)
          (transition)
          (save-game))
      "FAILED")))

(defn vote [gameid playerid vote]
  (perform-validated-action-on-gamestate
   valid-to-vote-for-team?
   vote-for-team
   resolve-team-selection
   gameid
   playerid
   vote))

(defn quest [gameid playerid questpass]
  (perform-validated-action-on-gamestate
   valid-to-vote-for-mission?
   vote-for-mission
   resolve-mission
   gameid
   playerid
   questpass))

(defn choose-team [gameid playerid team]
  (perform-validated-action-on-gamestate
   valid-to-propose-team?
   propose-team
   noop
   gameid
   playerid
   team))

(defn assassinate-merlin [gameid playerid guess]
  (perform-validated-action-on-gamestate
   valid-to-guess-merlin?
   guess-merlin
   noop
   gameid
   playerid
   guess))

(defn add-new-player [gameid playerid position]
  (let [gamestate (retrieve-game gameid)]
    (if  (valid-to-add-player? gamestate playerid)
      (-> gamestate
          (add-player playerid position)
          (resolve-adding-players)
          (save-game))
      "FAILED")))

(def game-attribute
  {:name "GameTheFirst"
   :num-players 4
   :roles [:percival, :morgana]})

(defn valid-game? [attributes]
  (<= (+ (count (:roles attributes)) 2) (:num-players attributes)))

(defn new-game [attributes]
  (if (valid-game? attributes)
    (let [roles-for-this-game (roles-for (:num-players attributes) (:roles attributes))]
      (make-new-game
       {:settings attributes
        :players []
        :unassigned-roles roles-for-this-game}))
    "FAILED"))



;(let [id (new-game {:name "test" :num-players 4 :roles [:percival]})]
;  (add-new-player id "Porter" 0)
;  (add-new-player id "Armaan" 0)
;  (add-new-player id "Takashi" 0)
;  (add-new-player id "Evan" 0)
;  (add-new-player id "Ben" 0))
