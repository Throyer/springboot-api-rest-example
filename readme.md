# users api
  > 🚨 Important, don't forget to grant execution permission to the scripts: `chmod -R +x .docker/scripts`

  - run tests
    ```bash
    .docker/scripts/mvn test
    ```

  - run `integration` tests
    ```bash
    .docker/scripts/mvn verify -P integration-tests
    ```
