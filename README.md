# github_mock_event_emitter

The github_mock_event_emitter is a simple service that is intended to be used in conjunction with the [github-scoring-service](https://github.com/robert-pierce/github_scoring_service). 

The intention of the service is to act as a means of testing the github-scoring-service by serving as a source of events to be consumbed by the scoring-service. 

The github_mock_event_emitter is a simple http server that listens for certain requests. 

When receiving a request the service will send another http request to the scoring-service in a format it expects. 

Thus, by utilizing this service you can populate the database for the github-scoring-service without having to be plugged into live github webhooks.


## Running
This service is intended to be run in conjunction with the [github-scoring-service](https://github.com/robert-pierce/github_scoring_service). See the documentation for the [github-scoring-service](https://github.com/robert-pierce/github_scoring_service) for more information on running the service in this fashion.


If you would like to compile this source code directly then you will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

To start a web server for the application, run:

    lein ring server

## License

Copyright © 2018 FIXME
