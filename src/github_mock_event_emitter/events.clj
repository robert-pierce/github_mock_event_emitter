(ns github-mock-event-emitter.events
  (:require  [clj-http.client :as client]
             [cheshire.core :refer :all]
             [clj-log.core :refer :all]
             [github-mock-event-emitter.config :as c]))

(defstruct repository :full_name)
(defstruct sender :login)
(defstruct event-body :sender :repository)
(defstruct event-header "x-github-event" "x-github-delivery")

(defn get-guid
  "This function will return a guid"
  [] 
  (.toString (java.util.UUID/randomUUID)))

(defn send-mock-event
  "This function will send a post request to the events endpoint
  on the scoring service."
  [type user repo]
  (try
    (let [guid (get-guid)
          user-struct (struct sender user)
          repo-struct (struct repository repo)
          event-body  (generate-string (struct event-body user-struct repo-struct))
          event-header (struct event-header type guid)]
      (client/post (format "%s/event" (c/config :scoring-service-url)) 
                   {:body event-body
                    :headers event-header
                    :content-type :json
                    :socket-timeout 1000  ;; in milliseconds
                    :conn-timeout 1000    ;; in milliseconds
                    :accept :json}))
    (catch Exception e
      (log :warn (str "There was an exception in send-mock-event. Exception " e))
      (throw e))))

(defn simulate-activity-recur
  [num-events types users repos]
  (loop [x num-events]
    (when (> x 0)
      (let [type (rand-nth types)
            user (rand-nth users)
            repo (rand-nth repos)]
        (send-mock-event type user repo))
      (recur (- x 1)))))

(defn simulate-activity
  "This function will take in information to simulate many requests. 
  This will simulate activity on some repositories. Helpful for initializing 
  an empty database with data to test."
  [num-events types users repos]
  (try
    (simulate-activity-recur num-events types users repos)
    (catch Exception e
      (log :warn (str "There was an exceptin in simulate-activity. Exeption " e)))))
