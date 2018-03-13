(ns github-mock-event-emitter.events-test
  (:require [clojure.test :refer :all]
            [clj-http.client :as client]
            [github-mock-event-emitter.config :as c]
            [github-mock-event-emitter.events :as events]))

(def mock-config {:scoring-service-url "some-url"})
(def expected-event-body "{\"sender\":{\"login\":\"some-user\"},\"repository\":{\"full_name\":\"some-repo\"}}")

(deftest send-mock-event-test
  (testing "It correctly sends an event"
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
  (testing "It correctly handles an exception"
    (with-redefs [client/post (fn [num-events types users repos] (throw (Exception. "some-exception")))]
      (try
        (events/send-mock-event "some-type" "some-user" "some-repo")  
        (catch Exception e
          (is (instance? Exception e)))))))

(deftest simulate-activity-test
  (testing "It correctly simulates some activity"
    (with-redefs [events/simulate-activity-recur (fn [num-events types users repos] {:num-events num-events :types types :users users :repos repos})]
      (let [result (events/simulate-activity 42 ["some-type"] ["some-user"] ["some-repo"])
            num-events (:num-events result)
            type (first (:types result))
            user (first (:users result))
            repo (first (:repos result))]
        (is (= 42 num-events))
        (is (= "some-type" type))
        (is (= "some-user" user))
        (is (= "some-repo" repo)))))
  (testing "It correctly handles an exception"
    (with-redefs [events/simulate-activity-recur (fn [num-events types users repos] (throw (Exception. "some-exception")))]
      (try
        (events/simulate-activity 42 ["some-type"] ["some-user"] ["some-repo"])  
        (catch Exception e
          (is (instance? Exception e)))))))
