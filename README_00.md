# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)


## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following ClientCommands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These ClientCommands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```


[My Web-API SequenceDiagram](https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYsgwr5ks9FMr73NWcz6K8Sy7JMALnAWspqig5QIAePKwvuh6ouisTYgmhgumGbpkhSBq0uWo4mkS4YWhy3K8gagrCjAwGhpRbqdrh5T0dojrOgSBEsh6uqZLG-rTiGrrSpGHLRjAIkOhJ5pJpeCHlCJWjyDxibJrBJboTy2a5pg-4gvBJRXAMZGjAuk59NOs7NuOrZ9N+V4mYU2Q9jA-aDr0QGWSBNl2Y2zb-GYnCrt4fiBF4KDoHuB6+Mwx7pJkmAeReRTUNe0gAKK7jl9Q5c0LQPqoT7dEFc7tr+ZxAiWAA8lXoPkRk6TVHEwMh9iJWhCW+phGLYqYuEavxpLkmAIkBkG9loENCkRoUlq0TGQYMWETVoCxTJsUpmVOvKslrdxml4XxrECRwKDcMJQawnJ2jzWNi3sqU0jXRShgPRpC3sXKiHkD44YAGZKkdcbaDAkLDBAVDACqp1ssZ5ToYlBkIHmbV-VlaaATALk4526VgH2A5DjAFMHKYy4ReugSQrau7QjAADio6sslp5peezCmdeLMFcV9ijhVM3BVtP5I215SNWLc4tcZbIdcgsRs6MqhodCatqAN2GI-hF2kjAE1TZtFE7ZJS00Tyq0Qz9z3YwdANqdxI0GxbRsq2A2sazA5tmi9y02zA8MIKzo7yQ7e0dXAEDKig4BPjAEDA5U2uMo0w3-VLdVpkzqvs+jmO547AFTML6vjJU-QVygACS0hVwAjL2ADMAAs0zdH0J6ZAaFafF3846AgoANv3o5QUPfS1wAcqODz7DAjQE8ce3E6TPmATP7NVxUNejg3zdt533e9-q-lT93I9jxPVmD93c8L5MS8r6YYUrp4kUbtgPhQNg3B4BCUMNrFIKUzw5F5spMylRagNCFiLYIct0BDifqMVeHZtK5xlptfIQ4d6jHnvfRcrUS6mXBDAT0eptawjgMA7WussT63Oh7d0xsKSm2QXNbaAdqJcmDt9YAPCqLR3+qpY6Gk3YsIDuUKhmQaFoJQP7ERVtSh0K9JkEOMA0AoGSMbUcwjdrkMOunE6bssGpnKOo6ho4i6kNTKXXG5dD6N3KC3Du+NqpEx5pvcmBD66uJgO49uniaZfzpgESw11kJ6IAFIQB5OHUYgQb4gAbNzSBStXLlGqJSO8LRa6izrOLIcADgBRKgLHZCUBZi1wbhgmqyMYCy2KfLPxZSKlVOgCsAA6iwOuhUWgACFdwKDgAAaRmN0Zxowj5uJPp4xWxiAYACsEloBofEnkDCUBokGswmQz0iKTTumbQxltXorXBsGbiv1REqWufGbO7sZHsO9qOWEdTpDKN2qoq5pj7aGwjMs8oAKhFSMOUC8ofh1IoBoWCn5FzLR8mwLCpJKBBQp30bM6QWcEKdiaVsjZti1CGSWXzJxlMvHrx8V5MmvlQnhXCVFAIXhyldi9LAYA2AAGEHiIkMBXNiZZJxjk3K+VCrFWMNSv80tSwU0WVjEFlDuB4FhDhZ50jwyyNVVAA0GtEWKVUZEGGNAQ7KnZExDoGgDkLR1Zyn26rzlGteia2GhhQ6WtrjaiFdqVWcqmoawOHI3Vms9cYEOXCbV4qdgSuViBOV2PJdAsuVLJY0sgb4hlS5wpAA)
