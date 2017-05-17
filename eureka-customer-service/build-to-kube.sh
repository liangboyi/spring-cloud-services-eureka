mvn clean package
scp target/eureka-customer-service-0.0.1-SNAPSHOT.jar 52.80.12.11:~/eureka-customer-service.jar
ssh 52.80.12.11 "scp eureka-customer-service.jar 10.14.14.60:~/demo/eureka-customer-service/"
