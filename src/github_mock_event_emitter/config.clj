(ns github-mock-event-emitter.config
  (:require [environ.core :refer [env]]))

(def config
  "This map represents the values needed by the service
  that are loaded from the local environment"
  {:scoring-service-url (env :scoring-service-url)})
