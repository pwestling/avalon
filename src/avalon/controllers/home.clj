(ns avalon.controllers.home
  (:require [avalon.models.game :as game])
  (:require [avalon.views.home :as view]))

(defn index [request]
  (let []
    (view/index (game/all))))
