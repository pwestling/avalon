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

(defn select-team [game-state team-size]
  (let
    [players (game/players game-state)]
    (layout
      [:div
        [:h3 { :style "text-align:center" } (str "Pick a team of " team-size)]
        (form-to [:post (str "/game/" (:id game-state) "/propose")]
          [:select.form-control
            { :multiple true :name "team[]" }
            (select-options (map (fn [p] [(:name p) (:id p)]) players))]
          [:br]
          (submit-button { :class "btn btn-block btn-lg btn-primary" } "Propose"))])))

(defn watch-team-selection [game-state leader team-size]
  (layout
    [:div (str (:name leader) " is picking a team of " team-size)]))

(defn team-summary [team]
  [:div.list-group
    (for [p team] [:div.list-group-item (:name p)])])

(defn vote [id leader team]
  (layout
    [:div
      [:h3 { :style "text-align:center" } (str (:name leader) " proposed the team:")]
      (team-summary team)
      [:div.row { :style "text-align:center" }
        (form-to { :class "col-xs-6" } [:post (str "/game/" id "/vote")]
          (hidden-field :vote "pass") ;figure out how to use a boolean instead of pass/fail
          [:button.btn.btn-success.btn-block "Support"])
        (form-to  { :class "col-xs-6" } [:post (str "/game/" id "/vote")]
          (hidden-field :vote "fail")
          [:button.btn.btn-warning.btn-block "Reject"])]]))

(defn mission [id team]
  (layout
    [:div
      [:div (str "You are on a mission with:")]
      (team-summary team)
      [:div.row { :style "text-align:center" }
        (form-to { :class "col-xs-6" } [:post (str "/game/" id "/quest")]
          (hidden-field :vote "pass") ;figure out how to use a boolean instead of pass/fail
          [:button.btn.btn-success.btn-block "Pass"])
        (form-to  { :class "col-xs-6" } [:post (str "/game/" id "/quest")]
          (hidden-field :vote "fail")
          [:button.btn.btn-warning.btn-block "Fail"])]]))

(defn watch-mission [team]
  (layout
    [:div
      [:div "The following people are on a mission:"]
      (team-summary team)]))

(defn waiting-for-votes [leader team]
  (layout
    [:div
      [:h3 { :style "text-align:center" } (str "Waiting for people to vote for team proposed by " (:name leader) ":")]
      (team-summary team)]))

(defn no-stage [game-state]
  (layout
    [:div "Unknown stage"]))
