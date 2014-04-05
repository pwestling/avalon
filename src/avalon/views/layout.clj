(ns avalon.views.layout
  (:use hiccup.core hiccup.page hiccup.form))

(defn layout [body]
  (html
    [:head
      (include-css "//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css")
      (include-js "//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js")]
    body))
