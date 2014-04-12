(ns avalon.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [avalon.controllers.home :as home]
            [avalon.controllers.game :as game]
            [avalon.controllers.login :as login]
            [avalon.middleware.auth :as auth]))

(defroutes auth-routes
  (GET "/" [request] (home/index request))

  (GET "/new" [request] (game/newg request))
  (POST "/create" request (game/create request)) ;figure out how to destructure the origin url
  (GET "/game/:id" [id :as request] (game/show request id))
  (DELETE "/game/:id" [id :as request] (game/delete request id)))

(defroutes login-routes
  (GET "/login" [request] (login/index request))
  (POST "/login" [name :as request] (login/create request name)))

(defroutes main-routes
  (auth/with-auth auth-routes)
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site (routes login-routes main-routes)))
