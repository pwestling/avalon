(ns avalon.controllers.login
  (:use compojure.core ring.util.response)
  (:require [avalon.views.login :as view]
            [avalon.models.user :as user-model]))

(defn update-name [name])

(defn index [request]
  (view/index))

(defn store-user [request user-id]
  (update-in request [:cookies "user-id"] (fn [i] user-id)))

(defn create [request name]
  (let
    [origin (get (:headers request) "origin")
     user-id (user-model/new-user { :name name })
     redirected-response (redirect origin)]
    (store-user redirected-response user-id)))
