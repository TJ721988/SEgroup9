# SEgroup9
Software Engineering Group 9 Neo4j
Lindiwe, Clifford, Thomas


Requirements:
To run our code as intended the following programs and packages are required:

	Neo4j v3.2.4
	Python v2.7(some newer version aren't compatible with py2neo, we had issues in v3.5)
	py2neo v3.1.2
	TextBlob v0.13.0
	Tweepy v.3.6.0
	Pycharm 2017.2.3 or a similar IDE or way of running a python script

Additionally 2 documents from this GitHub repository are required:
	Twitter_neo _final.py
	Cipher_Queries.txt



The above programs need to be installed and the packages downloaded(pip install <package>)

Once Neo4j is running open http://127.0.0.1:7474/ in a browser (realisitaclly we would have a server for this, and not connect to localhost)
The default password for the first run is "neo4j", on the first log in a password change is initiated our twitter_neo_ final.py script requires "neo4j" as the username and "neo" as the password. Once the correct password has been entered the browser logs into the Neo4j browser interface. (The python script can be edited, line 18, to accomodate a different user or password)
To build up the Neo4j database the Twitter_neo _final.py script must be run, which collects data from the tweepy API and runs it through TextBlob, to perform some text analysis. The script returns "done" if it connects and successfully imports data, however after a while twitter limits the data flow and we need to wait (about an hour), till more information can be imported(the script needs to be run again)



Ideally this part would be automated through a website/script, but for now Cipher queries must be entered in the top command line. 
1) :config initialNodeDisplay: 2000   		(increases max number of displayed nodes from 300)
2) MATCH (n) return n 						(returns all nodes)
This data can be limited/sorted through the queries found in the Cipher_Queries.txt file

