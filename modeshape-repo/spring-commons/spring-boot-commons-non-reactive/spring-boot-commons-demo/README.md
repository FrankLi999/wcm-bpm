1.
java -cp ~/.m2/repository/org/jasypt/jasypt/1.9.2/jasypt-1.9.2.jar  org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI input="Password@1" password=password algorithm=PBEWithMD5AndDES
mvn -Djasypt.encryptor.password=password spring-boot:run
