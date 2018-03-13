(ns github-mock-event-emitter.events-test
  (:require [clojure.test :refer :all]
            [clj-http.client :as client]
            [github-mock-event-emitter.config :as c]
            [github-mock-event-emitter.events :as events]))

(def mock-config {:scoring-service-url "some-url"})
(def expected-event-body "{\"sender\":{\"login\":\"some-user\"},\"repository\":{\"full_name\":\"some-repo\"}}")

(deftest send-mock-event-test
 (with-redefs [client/post (fn [url request] {:url url :request request})
               c/config mock-config
               events/get-guid (fn [& args] "some-guid")]
   (let  [result (events/send-mock-event "some-type" "some-user" "some-repo")
          url (:url result)
          request (:request result)
          body (:body request)
          headers (:headers request)]
     (is (= url "some-url/event"))
     (is (= expected-event-body body))
     (is (= "some-type" (get headers "x-github-event")))
     (is (= "some-guid" (get headers "x-github-delivery"))))))
