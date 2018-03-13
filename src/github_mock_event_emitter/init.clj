(ns github-mock-event-emitter.init
  (:require [clj-log.core :refer :all]
            [github-mock-event-emitter.config :as c]))

(defn dev-env-info
  []
  (log :info (str "Scoring Service Url: " (:scoring-service-url c/config))))

(defn check-scoring-service-connection
  []
  "Check connection")

(defn init-app
  "Any functions that you want to be called at app start up can go here. 
  Useful for checking the state of the app at startup (i.e. external service connection etc)"
  []
  (log :info "Initializing Github-Mock-Event-Emitter")
  (dev-env-info))
