(ns avalon.views.login)
(use 'hiccup.core 'hiccup.form)

(defn login []
  (html
    [:body
      (form-to [:post "/login"]
        [:div
          (label :name "Name")
          (text-field :name)]
        [:div
          (submit-button "Login")])]))
