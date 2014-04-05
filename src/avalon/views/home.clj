(ns avalon.views.home
  (:use hiccup.core avalon.views.layout))

(defn index [current-games]
  (layout
    [:body
      "Welcome to Avalon! Here are some games you can join:"
      [:ul
      (for [g current-games]
        [:li
          [:a { :href (str "/game/" (g :name)) } (g :name)]])
      [:div
        [:a {:href "/new"} "Create a game"]]]]))
