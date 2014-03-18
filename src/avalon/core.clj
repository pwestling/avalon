(ns avalon.core)

(def roles
  { :loyal-servant #()
    :minion-mordred #()
    :merlin #()
    :morgana #()
    :percival #()
    :oberon #()})

(def steps
  [:select-team :mission])


(def select-team
  {:leader 1
   :proposed-team [1 2 3]
   :votes {1 true 2 true 3 true 4 false}})

(def mission
  {:team [1 2 3]
   :votes {1 true 2 false 3 true}})

(def state
  {:players [1,2,3,4,5]
   :player-roles {1 :loyal-servant 2 :minion-mordred 3 :loyal-servant 4 :merlin 5 :minion-mordred}
   :rounds {0 {:select-team [select-team select-team]
                :mission mission}}})

(defn current-round [state]
  (apply max (keys (:rounds state))))

(defn done-voting-for-team [state]
  (= (count (:votes (last (get-in state [:rounds (current-round state) :select-team])))) (count (:players state))))

(done-voting-for-team state)

(defn vote-for-team [state player-id vote]
  (let [current-round (current-round state)
        teams (:select-team (get (:rounds state) current-round))
        team-index (- (count teams) 1)
        votes (:votes (get teams team-index))
        newstate (assoc-in state
                           [:rounds
                            current-round
                            :select-team
                            team-index]
                           (assoc votes player-id vote))]
    (if (done-voting-for-team newstate)
      ())))

(vote-for-team state 5 true)


