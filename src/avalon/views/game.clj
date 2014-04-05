(ns avalon.views.game
  (:use hiccup.core hiccup.form avalon.views.layout))

(declare propose)

(defn newg []
  (layout
    [:body
      (form-to [:post "/create"]
        [:div
          (label :name "Name of game")
          (text-field :name)]
        [:div
          (label :num-players "Number of players")
          (text-field :num-players)]
        [:div
          (label :percival "Percival")
          (check-box :percival)]
        [:div ;add validation to prevent morgana when percival isn't present
          (label :morgana "Morgana")
          (check-box :morgana)]
        [:div
          (label :oberon "Oberon")
          (check-box :oberon)]
        [:div
          (submit-button "Submit")])]))

(defn show [game-state]
  (case (:stage game-state)
    :propose (propose game-state)))

(defn propose [game-state]
  (let
    [players (:players game-state)]
    (layout
      [:body
        [:div "Propose a team:"]
        (form-to [:post "/propose"]
          [:select
            { :multiple true :name :team }
            (select-options players)]
          (submit-button "Propose"))])))
