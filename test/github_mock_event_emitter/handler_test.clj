(ns github-mock-event-emitter.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [github-mock-event-emitter.handler :refer :all]))

((deftest health-check-route-test
  (testing "It correctly handles a health-check request"
    (let [response (app-handler (mock/request :get "/health_check"))]
      (is (= (:status response) 200))
      (is (= (:body response) "I'm Alive")))))

  (testing "not-found route"
    (let [response (app-handler (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
