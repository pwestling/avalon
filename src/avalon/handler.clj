(ns avalon.handler
  (:use compojure.core avalon.globals)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [avalon.controllers.home :as home]
            [avalon.controllers.game :as game]
            [avalon.controllers.game-actions :as game-actions]
            [avalon.controllers.login :as login]
            [avalon.middleware.auth :as auth]))

(defroutes resource-routes
  (GET "/" [request] (home/index request))
  (GET "/new" [request] (game/newg request))
  (POST "/create" request (game/create request)) ;figure out how to destructure the origin url
  (GET "/game/:id" [id :as request] (game/show request id))
  (DELETE "/game/:id" [id :as request] (game/delete request id)))

(defroutes game-action-routes
  (GET "/game/:id/join" [id :as request] (game-actions/join request id))
  (POST "/game/:id/propose" [id :as request] (game-actions/propose request id))
  (POST "/game/:id/vote" [id :as request] (game-actions/vote request id))
  (POST "/game/:id/quest" [id :as request] (game-actions/quest request id)))

(defroutes login-routes
  (GET "/login" [request] (login/index request))
  (GET "/logout" [request] (login/destroy request))
  (POST "/login" [name :as request] (login/create request name)))

(defroutes main-routes
  (auth/with-auth resource-routes)
  (auth/with-auth game-action-routes)
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site (routes login-routes main-routes)))
