(ns avalon.views.game
  (:use hiccup.core hiccup.form avalon.views.layout)
  (:require [clojure.string :as string]
            [avalon.models.game :as game]))

(declare select-team)
(declare vote)
(declare no-stage)
(declare overview)

(defn newg []
  (layout
    [:div
      (form-to [:post "/create"]
        [:div
          (text-field { :placeholder "Name" :class "form-control" } :name)]
        [:br]
        [:div (label :num-players "Number of players")]
        [:div
          [:select
            { :name :num-players :class "form-control" }
            (select-options [3, 4, 5, 6])]]
        [:br]
        [:div.row
          [:div.col-xs-4
            (label :percival "Percival")
            (check-box { :class "form-control" } :percival)]
          [:div.col-xs-4 ;add validation to prevent morgana when percival isn't present
            (label :morgana "Morgana")
            (check-box { :class "form-control" } :morgana)]
          [:div.col-xs-4
            (label :oberon "Oberon")
            (check-box { :class "form-control" } :oberon)]]
        [:br]
        [:div
          (submit-button { :class "btn btn-block btn-lg btn-primary" } "Create Game")])]))
