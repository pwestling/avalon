(ns avalon.models.game)

(def GAME-PROPOSAL
  {:name "LiveRamp"
   :stage :propose
   :players [["Takashi" 2] ["Chris" 3] ["Porter" 4] ["Jacob" 1]]})

(def GAME-VOTE
  {:name "LiveRamp"
   :stage :vote
   :team [["Takashi" 2] ["Chris" 3]]})

(defn all []
  (let [] [GAME-PROPOSAL GAME-VOTE]))

(defn find-game [name]
  (let [] GAME-VOTE))
