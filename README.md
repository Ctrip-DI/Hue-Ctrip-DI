Ctrip Data Infrastructure Hue Apps
------------

Overview
------------
 
We are going to add more apps into hue. Currently, we have monitor and spark sql. 
Monitor will show Hadoop dashbard and every metric in your cluster. Spark sql is going to let user submit his sql to execute job in spark cluster.

# Getting Started #

### Prerequisites ###
- Hadoop Cluster Invironment
- Ganglia Cluster
- hue-2.5.0-cdh4.6.0

#### Misc ####

- JDK Runtime: JDK6 (OpenJDK or Oracle JDK)
- Maven
- Git
- Tomcat (CATALINA_HOME being set)

## Installation ##


###Install Data Service:###

	$yum install tomcat
	$Create mysql table and the script is /di-data-service/src/main/resources/script/di.sql
	$Configured /di-data-service/src/main/resources/conf/di.properties for your invironment
	$cd Hue-Ctrip-DI/di-data-service; mvn clean install -DskipTests
	$copy di-data-service.war to tomcat

###Install monitor app:###

	$mv Hue-Ctrip-DI/monitor $HUE_HOME/apps
	$cd $HUE_HOME/apps
    $sudo ../tools/app_reg/app_reg.py --install monitor  

###Install spark sql app:###

	$mv Hue-Ctrip-DI/sparksql $HUE_HOME/apps
	$cd $HUE_HOME/apps
    $sudo ../tools/app_reg/app_reg.py --install sparksql

###hue.ini Config section for monitor###
Configs needed in hue.ini config file.

	[monitor]
    [[di-service]]
        di_data_service_url="http://localhost:8080/di-data-service/"

###hue.ini Config section for sparksql###
Configs needed in hue.ini config file.

	[sparksql]
    [[spark-service]]
        spark_sql_url="http://10.2.4.189:8089/di-data-service/spark"

UI Example
------------

###MapReduce Dashboard###

![Off-CLI Installation](https://github.com/Ctrip-DI/Hue-Ctrip-DI/blob/master/docs/mapreduce_dashboard.png)

###Hdfs Dashboard###

![Off-CLI Installation](https://github.com/Ctrip-DI/Hue-Ctrip-DI/blob/master/docs/hdfs_dashboard.png)

###Metric Dashboard###

![Off-CLI Installation](https://github.com/Ctrip-DI/Hue-Ctrip-DI/blob/master/docs/metric_monitor.png)

##Resources##

Developer Mail: xgliao@ctrip.com

##Copyright and License##

Copyright 2014 ctriposs

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.


