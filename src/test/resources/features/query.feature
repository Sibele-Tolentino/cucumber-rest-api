Feature: Query CPFs

  Background:
    Given baseUri is http://localhost:8888

  Scenario: Query cpfs with restrictions
    When I GET /api/v1/restricoes/97093236014
    Then response code should be 200
    And response body should contain O CPF 97093236014 tem problema

  Scenario: Query cpfs without restrictions
    When I GET /api/v1/restricoes/52695131020
    Then response code should be 204