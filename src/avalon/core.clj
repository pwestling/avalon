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
   :votes {1 true 2 true 3 true 4 false 5 false}})

(def mission
  :team [1 2 3]
  :votes {1 true 2 false 3 true})

(def state
  {:players [1,2,3,4,5]
   :player-names {1 "Takashi" 2 "Porter" 3 "Armaan" 4 "Vir" 5 "Chris"}
   :player-roles {1 :loyal-servant 2 :minion-mordred 3 :loyal-servant 4 :merlin 5 :minion-mordred}
   :rounds {}})
