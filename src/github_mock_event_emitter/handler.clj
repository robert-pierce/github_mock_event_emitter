(ns github-mock-event-emitter.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :as middleware]
            [github-mock-event-emitter.events :as event])
  (:use [slingshot.slingshot :only [throw+ try+]]))

(defn send-mock-event-handler
  "Handles a send-mock-event request"
  [request]
  (try+
   (let [body (:body request)
         type (get body "type")
         user (get body "user")
         repo (get  body "repository")]
     (println (str "The body is " request))
     (event/send-mock-event type user repo)
     {:status 200 :body "Event Sent"})
   (catch Exception e
     {:status 500 :body "There was a server error"})
   (catch #((and (>= 400 (:status %))) (< 500 (:status %)))
       {:status 500 :body "The http call to send the request returned some 4xx status"})
   (catch #((and (>= 500 (:status %))) (< 600 (:status %)))
       {:status 500 :body "The http call to send the request returned some 5xx status"})))


(defn simulate-activity-handler
  "Handles a simulate-activity request"
  [request]
  (try  
    (let [body (:body request)
          num-events  (get body "number_of_events")
          types (get body "types")
          users (get body "users")
          repos (get  body "repositories")]
      (event/simulate-activity num-events types users repos)
      {:status 200})
    (catch Exception e
      {:status 500 :body (str "There was a server error" e)})))

(defn health-check
  "A health check endpoint that will also print the git hash"
  []
  {:status 200 :body "I'm Alive"})

(defroutes app-routes
  (POST "/event" request (send-mock-event-handler request))
  (POST "/simulator" request (simulate-activity-handler request))
  (ANY "/health_check" [] (health-check))
  (route/not-found "Not Found"))

(def app-handler (-> app-routes
                     (middleware/wrap-json-response)
                     (middleware/wrap-json-body)
                     (wrap-defaults api-defaults)))
