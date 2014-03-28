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
  {:players [1,3,2,5,4]
   :player-roles {1 :loyal-servant 2 :minion-mordred 3 :loyal-servant 4 :merlin 5 :minion-mordred}
   :rounds {0 {:select-team [select-team select-team]
               :mission {:votes {3 false}}}}})

;Informational Functions

(defn current-round-index [state]
  (apply max (conj (keys (:rounds state)) -1)))


(defn current-round [state]
  (get-in state [:rounds (current-round-index state)]))

(defn current-team-selection [round]
  (last (:select-team round)))

(defn current-mission [round]
  (:mission round))

(defn current-team-selection-index [round]
  (dec (count  (:select-team round))))

(defn done-voting? [state votes]
  (= (count votes)
     (count (:players state))))

(defn done-voting-for-team? [state]
  (done-voting? state (:votes (current-team-selection state))))

(defn done-voting-for-mission? [state]
  (done-voting? state (:votes (current-mission state))))

(defn current-leader [state]
  (:leader (current-team-selection (current-round state))))

(defn valid-to-vote-for-team [state player-id]
  (and (= (:phase (current-phase state)) :select-team)
       (= nil (get (:votes (current-team-selection (current-round state))) player-id))))

(defn current-phase [state]
  {:round (current-round-index state)
   :phase (if (done-voting-for-team? state) :mission :select-team)})

;New State functions


(defn next-player [state player-id]
  (let [players (:players state)]
    (get players
         (mod
          (inc (.indexOf players player-id))
          (count players)))))

(defn update-round [state round-index func]
  (update-in state [:rounds round-index] func))

(defn update-current-round [state func]
  (update-in state [:rounds (current-round-index state)] func))

(defn update-team-selection [state round-index selection-index func]
  (update-in state [:rounds round-index :select-team selection-index] func))

(defn update-mission [state round-index func]
  (update-in state [:rounds round-index :mission] func))

(defn update-current-team-selection [state func]
  (update-team-selection state (current-round-index state) (current-team-selection-index (current-round state)) func))

(defn update-current-mission [state func]
  (update-mission state (current-round-index state) func))

(defn new-round [state]
  (update-round state (inc (current-round-index state)) (constantly {})))

(defn new-team-vote [state]
  (let [leader (next-player state (current-leader state))]
    (update-current-round
     state
     (fn [round] (update-in round [:select-team] #(vec (conj % {:leader leader})))))))

(defn vote-in [m player-id vote]
  (update-in m [:votes] #(assoc % player-id vote)))

(defn vote-for-team [state player-id vote]
  (update-current-team-selection
   state #(vote-in % player-id vote)))

(defn vote-for-mission [state player-id vote]
  (update-current-mission
   state #(vote-in % player-id vote)))

(defn propose-team [state player-id team]
  (update-current-team-selection state #(assoc % :proposed-team team)))

(defn resolve-team-selection [state ])

(vote-for-team state 5 true)
(vote-for-mission state 5 false)

(valid-to-vote-for-team state 5)



(-> {:players [1,2] :player-roles {1 :loyal-servant 2 :minion-mordred}}
    (new-round)
    (new-team-vote)
    (propose-team 1 [1])
    (vote-for-team 1 true)
    (vote-for-team 2 false))


