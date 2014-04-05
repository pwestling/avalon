(ns avalon.models.game)

(defn all []
  (let [] [{ :name "liveramp" }]))

(defn find-game [name]
  {:name "LiveRamp"
   :stage :propose
   :players [["Takashi" 2] ["Chris" 3] ["Porter" 4] ["Jacob" 1]]})
