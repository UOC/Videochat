VideoChat
=========

This tool will allow for videoconference sessions with up to 6 people, using audio or video. 
The main idea is to make it easy for students to set up a session without the intervention of the teacher and to record and archive the recording without the need of installing additional software. There are two interfaces: the recorder and the player. 
The recorder should distinguish itself from already existent videoconferencing systems by its ease of use, recording and archiving which should be its main features. The player's most distinctive functionality is the solo/mute buttons which allow teachers to listen to the group or to a specific learner in isolation. There will be no moderator or administrator roles, all participants will have the same profile and it will be designed specifically for language students carrying out synchronous tasks.

Installation
============

1.	Install the eclipse IDE or NetBeans IDE.
2.	Download/clone from github https://github.com/UOC/VideoChat.
3.	Open the IDE selected, import the Videochat project. To achieve this just select the import option and search for the downloaded Videochat project on your hard drive.
4.	Edit the videochat.properties, here you can find two basic properties, the IP direction to the WoWza server and the parameters of the database. 
5.	Select the IP of the server you want to edit and also the database properties.
6.	Now build the project, a videochat.war will be generated.
7.	With the .sql file provided, import to the SQL architecture you choose.
8.	When all these steps are finished, deploy the videochat.war that is store on the deploy folder of the server you have chosen.

