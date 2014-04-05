(ns avalon.controllers.home
  (:require [avalon.models.game :as game])
  (:require [avalon.views.home :as view]))

(defn current-player [session]
  (:player session))

(defn index [session]
  (let [current-player (current-player session)]
    (view/index (game/all))))
