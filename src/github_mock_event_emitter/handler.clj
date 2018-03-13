(ns github-mock-event-emitter.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :as middleware]))

(defn health-check
  "A health check endpoint that will also print the git hash"
  []
  {:status 200 :body "I'm Alive"})

(defroutes app-routes
  (ANY "/health_check" [] (health-check))
  (route/not-found "Not Found"))

(def app-handler (-> app-routes
                     (middleware/wrap-json-response)
                     (middleware/wrap-json-body)
                     (wrap-defaults api-defaults)))
