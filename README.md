# FA³ST Endpoint OPC UA

![FA³ST Endpoint OPC UA Logo Light](./docs/images/fa3st-endpoint-opcua-positive.svg/#gh-light-mode-only "FA³ST Endpoint OPC UA Logo")
![FA³ST Endpoint OPC UA Logo Dark](./docs/images/fa3st-endpoint-opcua-negative.svg/#gh-dark-mode-only "FA³ST Endpoint OPC UA Logo")

This is an extension to [Eclipse FA³ST Service](https://github.com/eclipse-fa3st/fa3st-service) providing an implementation of the `Endpoint` interface for OPC UA.
It allows you to add an OPC UA endpoint/server to your FA³ST Service.

## Usage

Maven dependency
```xml
<dependency>
	<groupId>de.fraunhofer.iosb.ilt.fa3st.service</groupId>
	<artifactId>fa3st-service-endpoint-opcua</artifactId>
	<version>1.0.0-SNAPSHOT</version>
</dependency>
```

Gradle dependency
```gradle
implementation 'de.fraunhofer.iosb.ilt.fa3st.service:fa3st-service-endpoint-opcua:1.0.0-SNAPSHOT'
```

## General

The OPC UA Endpoint allows accessing data and execute operations within the FA³ST Service via [OPC UA](https://opcfoundation.org/about/opc-technologies/opc-ua/).

Unfortunately, there is currently no official mapping of the AAS API to OPC UA for AAS v3.0.
Nevertheless, we decided to still provide an OPC UA endpoint even though it is not (yet) standard-compliant.
This implementation is based on the [OPC UA Companion Specification OPC UA for Asset Administration Shell (AAS)](https://opcfoundation.org/developer-tools/specifications-opc-ua-information-models/opc-ua-for-i4-asset-administration-shell/) which defines a mapping between AAS and OPC UA for AAS v2.0 enriched with some custom adjustments and extensions to be used with AAS v3.0.

The OPC UA Endpoint is built with the [Prosys OPC UA SDK for Java](https://www.prosysopc.com/products/opc-ua-java-sdk/) which means in case you want to compile the OPC UA Endpoint yourself, you need a valid license for the SDK (which you can buy [here](https://www.prosysopc.com/products/opc-ua-java-sdk/purchase/).
For evaluation purposes, you also have the possibility to request an [evaluation license](https://www.prosysopc.com/products/opc-ua-java-sdk/evaluate).
The developers of the Prosys OPC UA SDK have been so kind to allow us to publish that pre-compiled version as part of this open-source project under the condition that all classes related to their SDK are obfuscated.


## Configuration Parameters

OPC UA Endpoint configuration supports the following configuration parameters

| Name                                      | Allowed Value                                                                                        | Description                                                                                                                        | Default Value                                                              |
| ----------------------------------------- | ---------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------- |
| discoveryServerUrl<br>*(optional)*        | String                                                                                               | URL of the discovery server.<br>If empty, discovery server registration is disabled.                                               |                                                                            |
| secondsTillShutdown<br>*(optional)*       | Integer                                                                                              | The number of seconds the server waits for clients to disconnect                                                                   | 2                                                                          |
| serverCertificateBasePath<br>*(optional)* | String                                                                                               | Path where the server application certificates are stored                                                                          | PKI/CA                                                                     |
| supportedAuthentications<br>*(optional)*  | Anonymous<br>UserName<br>Certificate                                                                 | List of supported authentication types                                                                                             | Anonymous                                                                  |
| supportedSecurityPolicies<br>*(optional)* | NONE<br>BASIC128RSA15<br>BASIC256<br>BASIC256SHA256<br>AES128_SHA256_RSAOAEP<br>AES256_SHA256_RSAPSS | List of supported security policies                                                                                                | NONE,<br>BASIC256SHA256,<br>AES128_SHA256_RSAOAEP,<br>AES256_SHA256_RSAPSS |
| tcpPort<br>*(optional)*                   | Integer                                                                                              | The port to use for TCP                                                                                                            | 4840                                                                       |
| userMap<br>*(optional)*                   | Map<String, String>                                                                                  | A map containing usernames and password.<br>If *UserName* is not included in `supportedAuthentications`, this property is ignored. | *empty*                                                                    |
| userCertificateBasePath<br>*(optional)*   | String                                                                                               | Path where the certificates for user authentication are saved                                                                      | USERS_PKI/CA                                                               |

### Certificate Management

The path provided with the `serverCertificateBasePath` configuration property stores the server and client application certificates and contains the following subdirectories

- `/certs`: trusted client certificates
- `/crl`: certificate revocation list for client certificates
- `/issuers/certs`: certificates of trusted CAs
- `/issuers/crl`: certificate revocation list for CA certificates
- `/issuers/rejected`:	rejected CA certificates
- `/private`: certificates for the OPC UA server
- `/rejected`: unkown/rejected client certificates

To provision the OPC UA Endpoint to use an existing certificate for the server, save the certificate file as `{serverCertificateBasePath}/private/FA3ST Service Endpoint OPC UA@{hostname}_2048.der` and the private key as `{serverCertificateBasePath}/private/FA3ST Service Endpoint OPC UA@{hostname}_2048.pem` where `{hostname}` is the host name of your machine.

When an unkown client connects to the OPC UA Endpoint, the connection will be rejected and its client certificate will be stored in `/rejected`.
To trust the certificate of a client and allow the connection, move the file to `/certs`.

The path provided with the `userCertificateBasePath` configuration property stores the user certificates and contains the following subdirectories

- `/certs`: trusted user certificates
- `/crl`: certificate revocation list for user certificates
- `/issuers/certs`: certificates of trusted CAs
- `/issuers/crl`: certificate revocation list for CA certificates
- `/issuers/rejected`:	rejected CA certificates
- `/rejected`: unkown/rejected client certificates

Similar to the client certificates, unknown user certificates are stored in `/rejected` the first time a new certificate is encountered.
To trust this certificate, simply move it to `/certs`.

and `userCertificateBasePath` point to directories where the corresponding certificates are stored.
These directories contain the following subdirectories:


```{code-block} json
:caption: Example configuration for OPC UA Endpoint.
:lineno-start: 1

{
	"endpoints": [ {
			"@class": "de.fraunhofer.iosb.ilt.fa3st.service.endpoint.opcua.OpcUaEndpoint",
			"discoveryServerUrl" : "opc.tcp://localhost:4840",
			"profiles": [ "AAS_REPOSITORY_FULL", "AAS_FULL", "SUBMODEL_REPOSITORY_FULL", "SUBMODEL_FULL" ],
			"userMap" : {
			"user1" : "secret"
			},
			"secondsTillShutdown" : 5,
			"serverCertificateBasePath" : "PKI/CA",
			"supportedSecurityPolicies" : [ "NONE", "BASIC256SHA256", "AES128_SHA256_RSAOAEP" ],
			"supportedAuthentications" : [ "Anonymous", "UserName" ]
			"tcpPort" : 18123,
			"userCertificateBasePath" : "USERS_PKI/CA",
	} ],
	//...
}
```

## OPC UA Client Libraries

To connect to the OPC UA Endpoint, you need an OPC UA Client. Here are some example libraries and tools you can use:

- [Eclipse Milo](https://github.com/eclipse/milo): Open Source SDK for Java.
- [Unified Automation UaExpert](https://www.unified-automation.com/downloads/opc-ua-clients.html): Free OPC UA test client (registration on website required for download).
- [Prosys OPC UA Browser](https://www.prosysopc.com/products/opc-ua-browser/): Free OPC UA test client (registration on website required for download).
- [Official Samples from the OPC Foundation](https://github.com/OPCFoundation/UA-.NETStandard-Samples): C#-based sample code from the OPC Foundation.

## Datatype mapping

It's necessary to map the AAS datatypes to OPC UA datatypes. In some cases, no corresponding datatype is available in OPC UA.
These datatypes are mapped to String.

The mapping is as follows

| AAS datatype                    | OPC UA datatype      | Comment                                   |
| ------------------------------- | -------------------- | ----------------------------------------- |
| xs:string                       | String               |                                           |
| xs:boolean                      | Boolean              |                                           |
| xs:decimal                      | String               |                                           |
| xs:integer                      | String               |                                           |
| xs:double                       | Double               | No distinction between +0.0 and -0.0      |
| xs:float                        | Float                | No distinction between +0.0 and -0.0      |
| xs:date                         | String               |                                           |
| xs:dateTime                     | DateTime             | Converted to UTC, TZ offset is lost.      |
| xs:time                         | String               |                                           |
| xs:gYear                        | String               |                                           |
| xs:gMonth                       | String               |                                           |
| xs:gDay                         | String               |                                           |
| xs:gYearMonth                   | String               |                                           |
| xs:gMonthDay                    | String               |                                           |
| xs:duration                     | String               |                                           |
| xs:byte                         | SByte                |                                           |
| xs:short                        | Int16                |                                           |
| xs:int                          | Int32                |                                           |
| xs:long                         | Int64                |                                           |
| xs:unsignedByte                 | Byte                 |                                           |
| xs:unsignedShort                | UInt16               |                                           |
| xs:unsignedInt                  | UInt32               |                                           |
| xs:unsignedLong                 | UInt64               |                                           |
| xs:positiveInteger              | String               |                                           |
| xs:nonNegativeInteger           | String               |                                           |
| xs:negativeInteger              | String               |                                           |
| xs:nonPositiveInteger           | String               |                                           |
| xs:hexBinary                    | ByteString           |                                           |
| xs:base64Binary                 | ByteString           |                                           |
| xs:anyURI                       | String               |                                           |

## API

As stated, there is currently no official mapping of the AAS API to OPC UA for AAS v3.0 but FA³ST Service implements its proprietary adaption of the mapping for AAS v2.0.

### Supported Functionality

- Writing values for the following types
	- Property
	- Range
	- Blob
	- MultiLanguageProperty
	- ReferenceElement
	- RelationshipElement
	- Entity
- Operations (OPC UA method calls). Exception: Inoutput-Variables are not supported in OPC UA.

### Not (yet) Supported Functionality

- Updating the model, i.e., adding new elements at runtime is not possible
- Writing values for the following types
	- DataSpecifications
	- Qualifier
	- Category
	- ModelingKind
- AASDataTypeDefXsd
	- Base64Binary
	- UnsignedInt
	- UnsignedLong
	- UnsignedShort
	- UnsignedByte

## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions are **greatly appreciated**.
You can find our contribution guidelines [here](.github/CONTRIBUTING.md)

## Contact

faaast@iosb.fraunhofer.de

## License

Distributed under the Apache 2.0 License. See `LICENSE` for more information.

Copyright (C) 2022 Fraunhofer Institut IOSB, Fraunhoferstr. 1, D 76131 Karlsruhe, Germany.

You should have received a copy of the Apache 2.0 License along with this program. If not, see https://www.apache.org/licenses/LICENSE-2.0.html.
