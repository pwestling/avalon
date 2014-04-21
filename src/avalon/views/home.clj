(ns avalon.views.home
  (:use hiccup.core hiccup.form avalon.views.layout))

(defn index [current-games]
  (layout
    [:div
      [:div { :class "list-group" }
      (for [game current-games]
        [:a { :class "list-group-item" :href (str "/game/" (:id game)) }
          (:name (:settings game))
          (form-to { :style "display:inline" } [:delete (str "/game/" (:id game))]
            (submit-button { :class "btn btn-xs pull-right" :style "margin-left: 10px" } "delete"))
          [:div { :class "badge" } (:num-players game)]])]
      [:div { :class "list-group" }
        [:a { :class "list-group-item active" :href "/new" :style "text-align:center" } "Start a new game"]]]))
