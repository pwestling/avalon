(ns avalon.core)

(def roles
  { :loyal-servant {:info-fn #() :side :good :necessary false}
    :minion-mordred {:info-fn #() :side :evil :necessary false}
    :merlin {:info-fn #() :side :good :necessary true}
    :morgana {:info-fn #() :side :evil :necessary false}
    :percival {:info-fn #() :side :good :necessary false}
    :oberon {:info-fn #() :side :evil :necessary false}
    :assassin {:info-fn #() :side :evil :necessary true}})

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
               :mission {:votes {3 true}}}}})

;util

(defn map-values [func m]
  (map #(func (second %)) m))

(defn half [n] (/ n 2))

(defn sum [coll] (reduce + coll))

;Informational functions

(defn current-round-index [state]
  (apply max (conj (keys (:rounds state)) -1)))


(defn current-round [state]
  (get-in state [:rounds (current-round-index state)]))

(defn current-team-selection [state]
  (last (:select-team (current-round state))))

(defn current-mission [state]
  (:mission (current-round state)))

(defn current-team-selection-index [state]
  (dec (count  (:select-team (current-round state)))))

(defn num-players [state]
  (count (:players state)))

(defn done-voting? [state thresh votes]
  (= (count votes)
     thresh))

(defn done-voting-for-team? [state]
  (done-voting? state (num-players state)(:votes (current-team-selection state))))

(defn done-voting-for-mission? [state]
  (done-voting? state (count (:team (current-mission state)))(:votes (current-mission state))))

(defn current-leader [state]
  (:leader (current-team-selection state)))

(defn vote-passed [threshold state votemap]
  (< threshold (count (filter #(= true (second %)) votemap))))

(defn team-vote-passed? [state]
  (vote-passed (half (num-players state)) state (:votes (current-team-selection state))))

(defn valid-to-vote-for-team? [state player-id]
  (and (= (current-phase state) :team-vote)
       (= nil (get (:votes (current-team-selection state)) player-id))))

(defn valid-to-vote-for-mission? [state player-id]
  (and (= (current-phase state) :mission)
       (= nil (get (:votes (current-mission state)) player-id))
       (some #(= player-id %)(:team (current-mission state)))))

(defn valid-to-propose-team? [state player-id]
  (and (= (current-phase state) :propose-team)
       (= player-id (current-leader state))))

(defn valid-to-guess-merlin? [state player-id]
  (and (= (current-phase state) :merlin-guess)
       (= :assassin (get (:player-roles state) player-id))))

(defn mission-failed? [mission]
  (and (some #(= false (second %)) (:votes mission))
       (= (count (:team mission)) (count (:votes mission)))))

(defn mission-passed? [mission]
  (and (not-any? #(= false (second %)) (:votes mission))
       (= (count (:team mission)) (count (:votes mission)))))

(defn mission-count [func state]
  (count (filter func (remove nil? (map-values :mission (:rounds state))))))

(defn mission-winner [state]
  (cond (< 2 (mission-count mission-passed? state)) :good
        (< 2 (mission-count mission-passed? state)) :evil
        :else nil))

(defn done-with-missions? [state]
  (or (= 5 (current-round-index state))
      (mission-winner state)))

(defn current-phase [state]
  (let [round-index (current-round-index state)
        round (current-round state)
        team-select (current-team-selection state)]
    (cond
     (done-with-missions? state) :merlin-guess
     (:mission round) :mission
     (:proposed-team team-select):team-vote
     :else :propose-team )))

(defn mandatory-roles [] (into {} (filter #(-> % val (:necessary)) roles)))


(defn roles-for [num-players special-roles]
  )


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
  (update-team-selection state (current-round-index state) (current-team-selection-index state) func))

(defn update-current-mission [state func]
  (update-mission state (current-round-index state) func))

(defn new-round [state]
  (update-round state (inc (current-round-index state)) (constantly {:select-team [{:leader (next-player state (current-leader state))}]})))

(defn new-team-vote [state]
  (let [leader (next-player state (current-leader state))]
    (update-current-round
     state
     (fn [round] (update-in round [:select-team] #(vec (conj % {:leader leader})))))))

(defn new-mission [state]
  (let [team (:proposed-team (current-team-selection state))]
    (update-current-mission state (constantly {:team team}))))

(defn vote-in [m player-id vote]
  (update-in m [:votes] #(assoc % player-id vote)))

(defn propose-team [state player-id team]
  (update-current-team-selection state #(assoc % :proposed-team team)))

(defn guess-merlin [state player-id merlin]
  (assoc state :merlin-guess player-id))

(defn resolve-team-selection [state]
  (if (done-voting-for-team? state)
    (if (team-vote-passed? state)
      (new-mission state)
      (new-team-vote state))
    state))

(defn resolve-mission [state]
  (if (and (not (done-with-missions? state))
           (done-voting-for-mission? state))
    (new-round state)
    state))

(defn vote-for-team [state player-id vote]
  (update-current-team-selection
   state #(vote-in % player-id vote)))

(defn vote-for-mission [state player-id vote]
  (update-current-mission
   state #(vote-in % player-id vote)))

(-> {:players [1,2] :player-roles {1 :loyal-servant 2 :minion-mordred}}
    (new-round)
    (new-team-vote)
    (propose-team 1 [1])
    (vote-for-team 1 true)
    (vote-for-team 2 false)
    (resolve-team-selection)
    (propose-team 2 [2])
    (vote-for-team 1 true)
    (vote-for-team 2 true)
    (resolve-team-selection)
    (vote-for-mission 2 true)
    (resolve-mission))
