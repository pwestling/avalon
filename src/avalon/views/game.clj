(ns avalon.views.game
  (:use hiccup.core hiccup.form avalon.views.layout))

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
