(ns avalon.game_interface (:require [taoensso.carmine :as car :refer (wcar)]
                                    [avalon.db :as db]))
(use 'avalon.core)


(def example-game-state {:id 10 :players [1,2,3] :player-roles {1 :loyal-servant 2 :assassin 3 :merlin} :rounds {0 {:select-team [{:leader 1}]}}})

(defn retrieve-game [gameid] (db/get-entry "game" gameid))
(defn save-game [state] (db/set-entry "game" (:id state) state))
(defn make-new-game [state] (db/new-entry state))

(save-game example-game-state)

(defn in-game [state playerid]
  (some #(= playerid %) (:players state)))

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


(def game-attribute
  {:name "GameTheFirst"
   :id 10
   :num-players 4
   :roles [:percival, :morgana]})

(defn valid-game? [attributes]
  (<= (+ (count (:roles attributes)) 2) (:num-players attributes)))

(defn new-game [attributes]
  (make-new-game
   {:players []
    :roles (roles-for (:num-players attributes) (:roles attributes))}))