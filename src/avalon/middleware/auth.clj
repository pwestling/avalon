(ns avalon.middleware.auth
  (:use compojure.core ring.util.response avalon.globals)
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
    (if (logged-in? request)
      (binding
        [active-user (db/get-entry "user" (:value (get (:cookies request) "user-id")))]
        (handler request))
      (redirect "/login"))))
