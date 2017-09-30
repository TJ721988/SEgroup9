import tweepy
from py2neo import Graph, authenticate
from py2neo import Node, Relationship
from textblob import TextBlob
from tweepy import Stream
from tweepy import OAuthHandler
from tweepy.streaming import StreamListener
import json
import textblob

#these are twitter authentication credentials
consumer_key="uev6x9obQ0UMTB7BL89GOgeGa"
consumer_secret="SjTsNljeyBU00hzSkoBjB4jUITxvaftUCDOvozqX06ahagv2nj"
access_token = "890200180787744769-JYBUjSqYaUQeKfoFkHzU8g70exhJq9U"
access_token_secret="6yRzJjCjxlN3hwlouwnHvyyONnYzYGkir0yfKgWkP3fBm"

#authenticating with neo4j pleaase note my username is neo4j and password is neo
authenticate("localhost:7474", "neo4j", "neo")
#creating graphs were we can add node note: topic is the twitter topic we are searching for
graph = Graph()
#file = open("newfile.txt","r")
var = "@realDonaldTrump"

#for line in file:
  #  var = line

topic = var
topic_string = '"' +var+'" '
topic_tweet = Node("Topic",name=topic)
#graph.create(topic_tweet)
graph.merge(topic_tweet)
topic_tweet = graph.evaluate('match(x:Topic{name:'+ topic_string+'}) return x')

#this function takes in json data from tweeter and store them in neo4j
def getVariables(data):
    #created_at = data.split('"created_at":"')[1].split('","id')[0]
   # twitterId = data.split(',"id":')[1].split(',"id_str')[0]
    #tweet = data.split(',"text":"')[1].split('","source')[0]
    #name = data.split(',"user":')[1].split()
    print(data)

    python_obj = json.loads(data)
    #here we are taking in data from json file and storing them in python variables
    created_at =python_obj["created_at"]
    twitterId = python_obj["id_str"]
    tweet = python_obj["text"]
    in_reply_to_screen_name = python_obj["in_reply_to_screen_name"]
    in_reply_to_user_id_str = python_obj["in_reply_to_user_id_str"]
    in_reply_to_status_id_str = python_obj["in_reply_to_status_id_str"]
    #print(in_reply_to_status_id_str)


    name = python_obj["user"]["name"]
    screen_name = python_obj["user"]["screen_name"]
    #user_M_Name = python_obj["Entities"]["user_mentions"]["name"]
    #user_M_screenName = python_obj["Entities"]["user_mentions"]["screen_name"]
    location = python_obj["user"]["location"]
    description = python_obj["user"]["description"]
    followers_count = python_obj["user"]["followers_count"]
    friends_count = python_obj["user"]["friends_count"]
    statuses_count = python_obj["user"]["statuses_count"]
    time_zone = python_obj["user"]["time_zone"]
    lang = python_obj["user"]["lang"]
    image = python_obj["user"]["profile_background_image_url"]
    user_created_at = python_obj["user"]["created_at"]
    user_id = python_obj["user"]["id_str"]
    longitude = ""
    latitude = ""
    #retweet = python_obj["user"]["retweeted_status"]
    coordinates = python_obj["coordinates"]

    #here is the libriary that does sentiment analysis
    analysis = TextBlob(tweet)
    polarity = analysis.sentiment.polarity
    subjectivity =analysis.sentiment.subjectivity
    sentiment=""
    subjective =""
    if (polarity<=0):
        sentiment ="negative"

    elif (polarity > 0):
        sentiment = "positive"
    if (subjectivity<=0):
        subjective ="opinion"
    elif (polarity>0):
        subjective="subjective"

    #print(name)

    #here we are creating person none for everytweet
    person = Node("Person", name=name,screen_name=screen_name,location=location,followers_count=followers_count,friends_count=friends_count,user_created_at=user_created_at)
    graph.create(person)
    tweet = Node("Tweet",name="T" , created_at= created_at,tweet=tweet,reply_user=in_reply_to_screen_name, reply_tweet_id = in_reply_to_user_id_str, sentiment= sentiment,subjective=subjective)
    graph.create(tweet)


    #here we are creating relationships for tweet and person
    Created_By = Relationship(tweet, "Created_By", person)
    Created_By.properties["name"] ="Created_By"
    graph.create(Created_By)

    if (in_reply_to_status_id_str!=None):
        reply = Node("Reply_Tweet", name="RT", id =  in_reply_to_status_id_str, screen_name =  in_reply_to_screen_name)
        graph.create(reply)
        replied_to = Relationship(person, "replied_to", reply)
        replied_to.properties["name"] = "replied_to"
        graph.create(replied_to)
    Generated_By =Relationship(tweet, "Generated_By", topic_tweet)
    Generated_By.properties["name"] = "Generated_By"
    graph.create(Generated_By)
    api = tweepy.API(auth)
    user = api.get_user(user_id)
    print("done")
'''
    #here we are creating friends links
    for friend in user.friends():
        friendo = Node("Person", name=friend.name, screen_name=friend.screen_name, location=friend.location, followers_count=friend.followers_count,
        friends_count=friend.friends_count)
        graph.create(friendo)
        is_a_friend =Relationship(person, "is_a_friend", friendo)
        is_a_friend.properties["name"] = "is_a_friend"
        graph.create(is_a_friend)

    #here we are creating followers links
    for followers in user.followers():
        follower = Node("Person", name=followers.name, screen_name=followers.screen_name, location=followers.location, followers_count=followers.followers_count,
        friends_count=followers.friends_count)
        graph.create(follower)
        Followed_by = Relationship(person, "Followed_by", follower)
        Followed_by.properties["name"] = "Followed_by"
        graph.create(Followed_by)
'''


def sentimentAnalysis(text):

    analysis = TextBlob(text)
    print (analysis.sentiment)

#this class listen to the twitter stream fed from twitter
class listener(StreamListener):
    def on_data(self, data):
        getVariables(data)
       # print(data)
       # tweet = data.split(',"text":"')[1].split('","source')[0]
        #print(tweet)
        #sentmentRating = sentimentAnalysis(tweet)
        return True

    def on_error(self, status):
        print (status)
        print ("testing")

#authentication with twitter
try:
    auth = OAuthHandler(consumer_key, consumer_secret)
    auth.set_access_token(access_token,access_token_secret)
    twitterStream = Stream(auth,listener())
    twitterStream.filter(track=[topic])
except:
    print("Tweepy bug or limit reached... start the program again for more data")
