# lru-cache


A cache built on the LRU eviction policy of data entities. 

Compile the cache server by:

$ javac TestRunnerSimple.java lrucache.java 

Start cache server by 

$ java TestRunnerSimple


Test server by connecting to server with telnet on port 9000. Send single key value and the server will respond with cached value.

$ telnet localhost 9000
