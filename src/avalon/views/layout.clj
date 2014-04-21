(ns avalon.views.layout
  (:use hiccup.core hiccup.page hiccup.form))

(defn layout [content]
  (html5 { :lang "en" }
    [:head
      [:meta { :charset "utf-8" }]
      [:meta { :http-equiv "X-UA-Compatible" :content "IE=edge" }]
      [:meta { :name "viewport" :content "width=device-width, initial-scale=1 user-scalable=no" }]
      (include-css "//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css")
      (include-js "//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js")]
    [:body
      [:nav { :class "navbar navbar-default navbar-static-top navbar-inverse" :role "navigation" }
        [:div.container-fluid
          [:div.navbar-header
            [:a { :class "navbar-brand" :href "/"} "Avalon"]
            [:ul.nav.navbar-nav.navbar-right
              [:li
                [:a { :href "/logout" } "Logout"]]]]]]
      [:div { :class "container" }
        content]]))
