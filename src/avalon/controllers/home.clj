(ns avalon.controllers.home
  (:use avalon.globals)
  (:require [avalon.models.game :as game]
            [avalon.views.home :as view]))

(defn index [request]
  (view/index (game/all)))
