(ns github-mock-event-emitter.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [github-mock-event-emitter.events :as event]
            [github-mock-event-emitter.handler :refer :all]))

(deftest send-mock-event-handler-test
  (testing "It correctly handles a send-mock-event-request"
    (with-redefs [event/send-mock-event (fn [& args] "event-sent")]
      (let [response (app-handler (mock/request :post "/event"))]
        (is (= (:status response) 200)))))
  (testing "It correctly handles an exception"
    (with-redefs [event/send-mock-event (fn [& args] (throw (Exception. "some-exception")))]
      (try
        (app-handler (mock/request :post "/event"))
        (catch Exception e
          (is (instance? Exception e)))))))

(deftest health-check-route-test
    (testing "It correctly handles a health-check request"
      (let [response (app-handler (mock/request :get "/health_check"))]
        (is (= (:status response) 200))
        (is (= (:body response) "I'm Alive")))))

(deftest not-found-route-test 
  (testing "not-found route"
    (let [response (app-handler (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
