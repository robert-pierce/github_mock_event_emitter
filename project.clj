(defproject github_mock_event_emitter "0.1.0-SNAPSHOT"
  :description "This is a service that sends mock github webhook requests to the github_scoring_service."
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.1"]
                 [clj-log "0.4.6"]
                 [ring/ring-json "0.4.0"]
                 [ring/ring-defaults "0.2.1"]
                 [clj-http "3.5.0"]
                 [cheshire "5.8.0"]
                 [slingshot "0.12.2"]
                 [environ "1.0.2"]]
  :plugins [[lein-ring "0.9.7"]
            [lein-environ "1.0.2"]]
  :ring {:handler github-mock-event-emitter.handler/app-handler
         :port 8010
         :init github-mock-event-emitter.init/init-app})
