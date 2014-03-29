(ns avalon.game-interface (:require [taoensso.carmine :as car :refer (wcar)]))
(use 'avalon.core)

(def redis-conn {:pool {} :spec {:host "127.0.0.1" :port 6379}})
(defmacro wcar* [& body] `(car/wcar redis-conn ~@body))

(def example-game-state {:id 10 :players [1,2] :player-roles {1 :loyal-servant 2 :minion-mordred}})

(defn retrieve-game [gameid] (wcar* (car/get (str "game" gameid))))
(defn save-game [state] (wcar* (car/set (str "game" (:id state)) state)))

(save-game example-game-state)




(retrieve-game 10)


