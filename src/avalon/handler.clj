(ns avalon.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [avalon.controllers.home :as home]
            [avalon.controllers.game :as game]
            [avalon.controllers.login :as login]
            [avalon.middleware.auth :as auth]))

(defroutes auth-routes
  (GET "/" {session :session} (home/index session))

  (GET "/new" [] (game/newg))
  (POST "/create" request (game/create request)) ;figure out how to destructure the origin url
  (GET "/game/:name" [name] (game/show name)))

(defroutes login-routes
  (GET "/login" [] (login/index))
  (POST "/login" [name :as request] (login/create name request)))

(defroutes main-routes
  (auth/with-auth auth-routes)
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site (routes login-routes main-routes)))
