(ns avalon.views.login
  (:use hiccup.core hiccup.form avalon.views.layout))

(defn index []
  (layout
    [:div
      (form-to [:post "/login"]
        [:div
          (label :name "Name")
          (text-field :name)]
        [:div
          (submit-button "Login")])]))
