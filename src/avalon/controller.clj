(ns avalon.controller
  (:use compojure.core ring.util.response)
  (:require [avalon.views.home :as home-view]
            [avalon.views.new-game :as new-game-view]
            [avalon.views.login :as login-view])) ;TODO: better way to include the views

;TODO: Move this logic somewhere else
(defn current-games []
  (let [] [{ :name "liveramp" }]))

(defn current-player [session]
  (:player session))

(defn logged-in []
  (let [] true))

(defn update-name [name])

; Controller Methods

(defn home [session]
  (let [current-player (current-player session)]
    ;(str current-player)
    (home-view/home (current-games))
    ))

(defn game [game-name]
  (if (not (logged-in))
    (redirect "/login")
    (str "Welcome to game: " game-name "!")))

(defn new-game []
  (new-game-view/new-game))

(defn create-game [request]
  (let
    [origin (get (:headers request) "origin")]
    (redirect origin)))

(defn login-page []
  (login-view/login))

(defn login [request name]
  (let
    [origin (get (:headers request) "origin")]
    (update-name name)
    (redirect origin)))
