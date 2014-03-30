(ns avalon.views.home)
(use 'hiccup.core)

(defn home [current-games]
  (html
    [:body
      "Welcome to Avalon! Here are some games you can join:"
      [:ul
      (for [g current-games]
        [:li
          [:a { :href (str "/join/" (g :name)) } (g :name)]])
      [:div
        [:a {:href "/new"} "Create a game"]]]]))
