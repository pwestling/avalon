(ns avalon.middleware.auth
  (:use compojure.core ring.util.response))

(defn logged-in? [request]
  true)

(defn with-auth [handler]
  (fn [request]
    (let [login-path "/login"]
      (if (logged-in? request)
        (handler request)
        (redirect login-path)))))
