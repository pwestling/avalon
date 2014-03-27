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
               :mission {:votes {3 false}}}}})

(defn current-phase [state]
  {:round (current-round-index state)
   :phase (if (done-voting-for-team? state) :mission :select-team)})

(defn current-round-index [state]
  (apply max (keys (:rounds state))))

(defn current-round [state]
  (get-in state [:rounds (current-round-index state)]))

(defn current-team-selection [round]
  (last (:select-team round)))

(defn current-mission [round]
  (:mission round))

(defn current-team-selection-index [round]
  (dec (count  (:select-team round))))

(defn done-voting-for-team? [state]
  (= (count (:votes (current-team-selection state)))
     (count (:players state))))

(defn update-current-team-vote [state votemap]
  (assoc-in state [:rounds
                   (current-round-index state)
                   :select-team
                   (current-team-selection-index (current-round state))
                   :votes]
            votemap))

(defn update-current-mission [state votemap]
  (assoc-in state [:rounds
                   (current-round-index state)
                   :mission
                   :votes]
            votemap))


(defn valid-to-vote-for-team [state player-id]
  (and (= (:phase (current-phase state)) :select-team)
       (:votes (current-team-selection (current-round state)))))

(defn vote-for-team [state player-id vote]
  (update-current-team-vote
   state (assoc (:votes (current-team-selection (current-round state))) player-id vote)))

(defn vote-for-mission [state player-id vote]
  (update-current-mission
   state (assoc (:votes (current-mission (current-round state))) player-id vote)))

(vote-for-team state 5 true)
(vote-for-mission state 5 false)





