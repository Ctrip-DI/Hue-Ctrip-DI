## DI-Data-Service ##

# Infrastructure #
 
DI data Service leverage Ganglia to get all of metric from Hdfs, Yarn, HBase, Spark and so on. The Service will also get the metric by JMX server or any other API from the cluster.

![Off-CLI Installation](https://github.com/Ctrip-DI/Hue-Ctrip-DI/blob/master/di-data-service/docs/di-data-service.png)

# How to build and deploy #
Create table by the script of /di-data-service/src/main/resources/script/di.sql

Configured /di-data-service/src/main/resources/conf/di.properties for some invironment

Depend on Maven, and run command under the folder:
mvn clean install

Put the war package to web container like ‘tomcat or jetty’
