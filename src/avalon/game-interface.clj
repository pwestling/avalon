(ns avalon.game-interface (:require [taoensso.carmine :as car :refer (wcar)]))
(use 'avalon.core)

(def redis-conn {:pool {} :spec {:host "127.0.0.1" :port 6379}})
(defmacro wcar* [& body] `(car/wcar redis-conn ~@body))

(def example-game-state {:id 10 :players [1,2,3] :player-roles {1 :loyal-servant 2 :assassin 3 :merlin} :rounds {0 {:select-team [{:leader 1}]}}})

(defn retrieve-game [gameid] (wcar* (car/get (str "game" gameid))))
(defn save-game [state] (wcar* (car/set (str "game" (:id state)) state)))

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



(choose-team 10 1 [1 2])
(vote 10 1 true)
(vote 10 2 true)
(vote 10 3 true)
(quest 10 1 true)
(quest 10 2 false)
(choose-team 10 2 [1 2])
(vote 10 1 false)
(vote 10 2 true)
(vote 10 3 false)
(choose-team 10 3 [1 3])
(vote 10 1 true)
(vote 10 2 false)
(vote 10 3 true)
(quest 10 1 true)
(quest 10 2 false)
(quest 10 3 true)
(choose-team 10 1 [1 3])
(vote 10 1 true)
(vote 10 2 false)
(vote 10 3 true)
(quest 10 1 true)
(quest 10 3 true)
(choose-team 10 2 [1 2])
(vote 10 1 false)
(vote 10 2 true)
(vote 10 3 false)
(choose-team 10 3 [1 3])
(vote 10 1 true)
(vote 10 2 false)
(vote 10 3 true)
(quest 10 1 true)
(quest 10 3 true)
(assassinate-merlin 10 2 3)




(current-phase (retrieve-game 10))
