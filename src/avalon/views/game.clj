(ns avalon.views.game
  (:use hiccup.core hiccup.form avalon.views.layout))

(declare propose)

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
            (select-options [1, 2, 3, 4, 5, 6])]]
        [:br]
;        [:div
;          { :class "btn-group btn-group" :data-toggle "buttons-checkbox" }
;          [:button { :type "button" :class "btn btn-default" } "Percival" ]
;          [:button { :type "button" :class "btn btn-default" } "Morgana" ]
;          [:button { :type "button" :class "btn btn-default" } "Oberon" ]]
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

(defn show [game-state]
  (case (:stage game-state)
    :propose (propose game-state)))

(defn propose [game-state]
  (let
    [players (:players game-state)]
    (layout
      [:div
        [:h3 { :style "text-align:center" } "Team Selection"]
        (form-to [:post "/propose"]
          [:select.form-control
            { :multiple true :name :team }
            (select-options players)]
          [:br]
          (submit-button { :class "btn btn-block btn-lg btn-primary" } "Propose"))])))
