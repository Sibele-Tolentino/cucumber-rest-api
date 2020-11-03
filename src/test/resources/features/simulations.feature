Feature: Simulations

  Background:
    Given baseUri is http://localhost:8888

  Scenario: Inserting a new simulation
    And I set body to {"nome": "Moises Arcoiro","cpf": 23732367029,"email": "moises@naoconsegue.com","valor": 1200,"parcelas": 3,"seguro": true}
    When I POST /api/v1/simulacoes
    Then response code should be 201
    And response body should be valid json

  Scenario: Returns all existing simulations
    When I GET /api/v1/simulacoes
    Then response code should be 200
    And response body should be valid simulations json

  Scenario: Returns a existing simulation
    When I GET /api/v1/simulacoes/23732367029
    Then response code should be 200
    And response body should be valid simulations json

  Scenario: Editing a existing simulation
    And I set body to {"nome": "Moises Raquete do Guga","cpf": 23732367029,"email": "moises@consegue.com","valor": 100,"parcelas": 6,"seguro": false}
    When I PUT /api/v1/simulacoes/23732367029
    Then response code should be 200
    And response body should be valid simulations json

  Scenario: Deleting a existing simulation
    When I GET /api/v1/simulacoes
    And response body path $.[2]id should exists
    And I DELETE simulation /api/v1/simulacoes/ with $.[2]id
    Then response code should be 200