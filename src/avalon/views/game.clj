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

(defn game-summary [game-state]
  [:div
    [:table.table.table-bordered
      [:tbody
        [:tr
          [:th "Name"]
          [:td (:name (:settings game-state))]]
        [:tr
          [:th "Roles"]
          [:td (string/join "," (:roles (:settings game-state)))]]
        [:tr
          [:th (str "Players" " (max " (:num-players (:settings game-state)) ")")]
          [:td (string/join "," (map :name (game/players game-state)))]]]]])

(defn overview [game-state open]
  (layout
    [:div
      (game-summary game-state)
      (if open
        [:a.btn.btn-primary.btn-block { :href (str "/game/" (:id game-state) "/join") } "Join"])]))

(defn select-team [game-state]
  (let
    [players (game/players game-state)]
    (layout
      [:div
        [:h3 { :style "text-align:center" } "Team Selection"]
        (form-to [:post "/propose"]
          [:select.form-control
            { :multiple true :name :team }
            (select-options players)]
          [:br]
          (submit-button { :class "btn btn-block btn-lg btn-primary" } "Propose"))])))

(defn vote [game-state]
  (let
    [team (:team game-state)]
    (layout
      [:div
        [:h3 { :style "text-align:center" } "Proposed Team"]
        [:div.list-group
          (for [player team]
            [:div.list-group-item (first player)])]
        [:div.row { :style "text-align:center" }
          (form-to { :class "col-xs-6" } [:post "/vote"]
            (hidden-field :pass true)
            [:button.btn.btn-success.btn-block "Support"])
          (form-to  { :class "col-xs-6" } [:post "/vote"]
            (hidden-field :pass true)
            [:button.btn.btn-warning.btn-block "Reject"])]])))

(defn no-stage [game-state]
  (layout
    [:div "Unknown stage"]))
