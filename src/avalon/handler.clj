(ns avalon.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [avalon.controllers.home :as home]
            [avalon.controllers.game :as game]
            [avalon.controllers.login :as login]))

(defroutes app-routes
  (GET "/" {session :session} (home/index session))

  (GET "/login" [] (login/index))
  (POST "/login" [name :as request] (login/create name request))

  (GET "/new" [] (game/newg))
  (POST "/create" request (game/create request)) ;figure out how to destructure the origin url
  (GET "/join/:name" [name] (game/show name))

  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
