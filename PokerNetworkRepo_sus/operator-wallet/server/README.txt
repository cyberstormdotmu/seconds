OPERATOR WALLET
This is a mock reference implementation of an Operator Wallet. It creates balances and keeps them in memory only. 
A new balance will be created for every unique currency requested.

* Runtime Configuration
The reference wallet uses JMX to expose runtime configuration where you can change specific behavior of the wallet.

* API Key verification
The API key of the operator is salted with the request id, MD5-hashed and included as a HTTP header named x-api-key.
You can use the hash of the API key to verify that the sender has access to the shared secret of your API key.

When running the mock reference server you can override this by setting a system property: -Dapikey.ignore

You can also set the API key by adding a operator-key.properties file in classpath with entry: operator.key=...


