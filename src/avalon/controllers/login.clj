(ns avalon.controllers.login
  (:use compojure.core ring.util.response)
  (:require [avalon.views.login :as view]
            [avalon.models.user :as user-model]))

(defn update-name [name])

(defn index []
  (view/index))

(defn store-user [request user-id]
  (update-in request [:cookies "user-id"] (fn [i] user-id)))

(defn create [name request]
  (let
    [origin (get (:headers request) "origin")
     user-id (user-model/new-user { :name name })
     redirected-response (redirect origin)]
    (store-user redirected-response user-id)))
