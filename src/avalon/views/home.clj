(ns avalon.views.home
  (:use hiccup.core avalon.views.layout))

(defn index [current-games]
  (layout
    [:div
      [:div { :class "list-group" }
      [:div { :class "list-group-item" :style "text-align:center; " } "Open Games"]
      (for [game current-games]
        [:a { :class "list-group-item" :href (str "/game/" (get game "id")) }
          (get game "name")
          [:div { :class "badge" } (get game "num-players")]])]
          [:div { :class "list-group" }
            [:a { :class "list-group-item active" :href "/new" :style "text-align:center" } "Start a new game"]]]))
