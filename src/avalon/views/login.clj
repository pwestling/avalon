(ns avalon.views.login
  (:use hiccup.core hiccup.form avalon.views.layout))

(defn index []
  (layout
    [:div
      (form-to [:post "/login"]
        [:div
          (text-field { :class "form-control" :placeholder "What's your name?" } :name)]
        [:br]
        [:div
          (submit-button { :class "form-control" } "Submit")])]))
