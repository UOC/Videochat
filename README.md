VideoChat
=========

This tool will allow for videoconference sessions with up to 6 people, using audio or video. 
The main idea is to make it easy for students to set up a session without the intervention of the teacher and to record and archive the recording without the need of installing additional software. There are two interfaces: the recorder and the player. 
The recorder should distinguish itself from already existent videoconferencing systems by its ease of use, recording and archiving which should be its main features. The player's most distinctive functionality is the solo/mute buttons which allow teachers to listen to the group or to a specific learner in isolation. There will be no moderator or administrator roles, all participants will have the same profile and it will be designed specifically for language students carrying out synchronous tasks.

Installation
============

## Videochat Java App

1.	Install the eclipse IDE or NetBeans IDE.
2.	Download/clone from github https://github.com/UOC/VideoChat.
3.	Open the IDE selected, import the Videochat project. To achieve this just select the import option and search for the downloaded Videochat project on your hard drive.
4.	Edit the videochat.properties, here you can find two basic properties, the IP direction to the Wowza (http://wowza.com) server and the parameters of the database. 
5.	Select the IP of the server you want to edit and also the database properties.
6.    Add the jars to your maven repository (from folder videochat/compiled_lib)

    mvn install:install-file -Dfile=lti-1.0.3.jar -DgroupId=edu.uoc -DartifactId=lti -Dversion=1.0.3 -Dpackaging=jar
    mvn install:install-file -Dfile=JavaUtils-1.1.2.jar -DgroupId=org.campusproject -DartifactId=JavaUtils -Dversion=1.1.2 -Dpackaging=jar
    
7.   Now build the project, a videochat.war will be generated.
8.   With the .sql file provided, import to the SQL architecture you choose.
9.   Configure the consumer key (edit src/main/resources/authorizedConsumersKey.properties)
     
    To create a consumer key named *test* with shared secret **SECRET123**
    
    
    consumer_key.*test*.enabled=1
    
    consumer_key.*test*.secret=**SECRET123**
    
    consumer_key.*test*.callBackUrl= 
    
    consumer_key.*test*.fieldSessionId=token
    
10.   When all these steps are finished, deploy the videochat.war that is store on the deploy folder of the server you have chosen.

## Videochat Wowza App
To create a Wowza application you will need and Wowza Eclipse (https://www.wowza.com/streaming/developers/wowza-ide-software-update). Basically there are 2 classes
* Videochat.java This is a Module that allows to interact Flash and Wowza (http://www.wowza.com/forums/content.php?12-server-side-modules-and-code-samples). Then you have to create a new life application on videochat (you will fine a template videochat-wowza/configuration_sample/template/)
     
* VideochatAPI.java It is a HTTP Provider (http://www.wowza.com/forums/content.php?182-How-to-get-detailed-server-info-with-an-HTTPProvider
 you have to enable it on conf/VHost.xml (see a template from /videochat-wowza/configuration_sample/template/conf/VHost.xml)and add a videochat API like
 
     <!-- Admin HostPort -->
                        <HostPort>
                                    .......
                                       <HTTPProvider>
                                                <BaseClass>edu.uoc.speakapps.VideochatAPI</BaseClass>
                                                <RequestFilters>videochatAPI*</RequestFilters>
                                                <AuthenticationMethod>admin-basic</AuthenticationMethod>
                                        </HTTPProvider>
# More Information
Speak Apps Project has been funded with support from the Lifelong Learning Programme of the European Commission. This document reflects only the views of the authors, and the European Commission cannot be held responsible for any use which may be made of the information contained therein. 
![EU Logo](http://www.speakapps.eu/wp-content/themes/speakapps/images/EU_flag.jpg)