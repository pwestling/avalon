(ns avalon.middleware.auth
  (:use compojure.core ring.util.response)
  (:require [avalon.models.user :as user-model]
            [avalon.db :as db]))

(defn logged-in? [request]
  (let
    [user-id (:value (get (:cookies request) "user-id"))]
    (and
      (not (nil? user-id))
      (user-model/exists? user-id))))

(defn with-auth [handler]
  (fn [request]
    (let [login-path "/login"]
      (if (logged-in? request)
        (let
          [user-id (:value (get (:cookies request) "user-id")) ;why am i getting the :value?
           user (db/get-entry "user" user-id)
           request-with-user (update-in request [:params "current-user"] (fn [i] (do user)))] ;there must be a better way to update the session
          (handler request-with-user))
        (redirect login-path)))))
