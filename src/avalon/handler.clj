(ns avalon.handler
  (:use compojure.core
        avalon.controller)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/" {session :session} (home session))

  (GET "/login" [] (login-page))
  (POST "/login" [name :as request] (login name request))

  (GET "/new" [] (new-game))
  (POST "/create" request (create-game request)) ;figure out how to destructure the origin url
  (GET "/join/:name" [name] (game name))

  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
