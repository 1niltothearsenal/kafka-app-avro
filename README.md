Start the zookeeper
zookeeper-server-start.bat config\zookeeper.properties

Start kafka
kafka-server-start.bat config\server.properties

Create a topic
kafka-topics.bat --zookeeper 127.0.0.1:2181 --topic first_topic --create --partitions 3 --replication-factor 1

List the topics
kafka-topics.bat --zookeeper 127.0.0.1:2181 --list

Describe the topics
kafka-topics.bat --zookeeper 127.0.0.1:2181 --topic first_topic --describe

NOW KAFKA CONSOLE PRODUCER
produce a message  ("Ctrl+c" to stop)
kafka-console-producer --broker-list 127.0.01:9092 --topic first_topic

produce a message with acks
kafka-console-producer --broker-list 127.0.01:9092 --topic first_topic --producer-property acks=all

NOTE: now when you send a message to a topic that does not exist,
kafka-console-producer.bat --broker-list 127.0.0.1:9092 --topic new_topic



The second time it works, because the first time it did not find the leader since the topic was being created. The second time it readjusts and the leader is already elected(as producers are able to recover from fails)

NOW KAFKA CONSOLE PRODUCER(broker list vs bootstrap server: do research)

bootstrap-server is required
kafka-console-consumer --bootstrap-server 127.0.0.1:9092 --topic first_topic

NOTE: here you will not see the message at first, open a new command prompt window and then use kafka-console-producer to create a new message, then the message will appear automatically in consumer, notice that its real time.

To show messages from the beginning
kafka-console-consumer --bootstrap-server 127.0.0.1:9092 --topic first_topic --from-beginning

Consumer Group
kafka-console-consumer.bat --bootstrap-server 127.0.0.1:9092 --topic first_topic --group my-first-application

NOTE: No messages seen as we are not reading from the beginning

Here, when a producer creates a message, it can be seen in consumer console..

BUT, when you create a new consumer with exact same command and above,
kafka-console-consumer.bat --bootstrap-server 127.0.0.1:9092 --topic first_topic --group my-first-application

AND you create multiple messages. The messages will randomly go to diff console, the messages are split as group id is same, they share the load.(there are 3 partitions)

Now,try this:
kafka-console-consumer.bat --bootstrap-server 127.0.0.1:9092 --topic first_topic --group my-second-application --from-beginning
First it will show all the messages, stop the consumer
Try again
kafka-console-consumer.bat --bootstrap-server 127.0.0.1:9092 --topic first_topic --group my-second-application --from-beginningIt will not show the messages this time, it will only read the new messages.
It is because, the group was specified and offsets have been committed in kafka, my second app has read all the messages upto message no.29

Now we use,(it worked w/o bat)
kafka-consumer-groups.bat
Try this
kafka-consumer-groups.bat --bootstrap-server localhost:9092 --list

When you use the console consumer and you don't specify a group, it generates random console consumer for with you with a random number, that is why you have four groups..

kafka-consumer-groups.bat --bootstrap-server localhost:9092 --describe --group my-second-application

NOTE: LAG = 0 means console consumer has read all the data

kafka-consumer-groups.bat --bootstrap-server localhost:9092 --describe --group my-first-application

NOTE: if you see LAG = some number, it means this consumer has not read all the messages

So , if you try 
kafka-console-consumer.bat --bootstrap-server 127.0.0.1:9092 --topic first_topic --group my-first-application --from-beginning

And then, you describe the first application again, you will see the LAG = 0

kafka-consumer-groups.bat --bootstrap-server localhost:9092 --describe --group console-consumer-……
You will see that the CURRENT-OFFSET and LAG is different again from the previous run

Try starting the my-first-application consumer and then running the describe command.

kafka-console-consumer.bat --bootstrap-server 127.0.0.1:9092 --topic first_topic --group my-first-application

kafka-consumer-groups.bat --bootstrap-server localhost:9092 --describe --group my-first-application

You will see which application and which machine is consuming your message.

RESETTING OFFSET

kafka-consumer-groups.bat



Enter this
kafka-consumer-groups.bat --bootstrap-server localhost:9092 --group my-first-application --reset-offsets --to-earliest
You will get error.

Enter this,

kafka-consumer-groups.bat --bootstrap-server localhost:9092 --group my-first-application --reset-offsets --to-earliest --execute
You will get error again.

kafka-consumer-groups.bat --bootstrap-server localhost:9092 --group my-first-application --reset-offsets --to-earliest --execute --topic first_topic

So, when you restart the consumer you will see all of the data all over again.
kafka-console-consumer.bat --bootstrap-server 127.0.0.1:9092 --topic first_topic --group my-first-application

Run the describe operation
kafka-consumer-groups.bat --bootstrap-server localhost:9092 --describe --group my-first-application

You will see the LAG is 0

Try this..
kafka-consumer-groups.bat --bootstrap-server localhost:9092 --group my-first-application --reset-offsets --shift-by -2 --execute --topic first_topic               

-2 for backwards, +2 for forwards

Try this again.

kafka-console-consumer.bat --bootstrap-server 127.0.0.1:9092 --topic first_topic --group my-first-application
