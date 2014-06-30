# jdbc-scala
This time around we will look at JDBC data access. I know, JDBC is blocking, but it still plays a large role in
todays IT infrastructure, and if we cannot talk to our JDBC databases, well, thats basically the end for our
Scala/Akka/Spray solutions, and thats not what we want. 

Three projects come to mind. I still like Spring, and the Spring templates are great. We can use the 
[spring-scala](https://github.com/spring-projects/spring-scala) project that makes it easier to use the Spring framework
in Scala. 

The other project is [ScalikeJdbc](http://scalikejdbc.org/) which is a SQL-based DB access library for Scala developers.
It has nice features I wish I had in Java like QueryDSL, SQL interpolation, a Query API and much more!  

The third one is from the Typesafe stack itself and it is called [Slick](http://slick.typesafe.com/). 
It is a modern database query and access library for Scala.

# spring-scala
The DI container is basically dead-and-gone because of the cake-pattern and Scala implicits, but I like the 
Spring template API, so let's use it. The spring-scala project makes using Spring templates very easy by providing
Scala-friendly wrappers for the Spring templates. I don't care much for the spring-beans feature.

# ScalikeJdbc
ScalikeJdbc is the project I will use more often. I like it very much, the queryDSL, the API, string interpolation 
and the integration with the Scala language. When you read the code, the JdbcTemplate reads like, "template, do this,
with these parameters, and be quick about it!", and the scalike reads more like I want this done, make it so! I always
wanted to end a sentence with these words :-)

# Slick
Slick is part of the Typesafe stack and provides multiple APIs to communicate with your JDBC database.I used the 
lifted embedding API where you as the programmer is not working with standard Scala types, but with types that are lifted
which can become a bit confusing.

# Starting
This project uses the Typesafe Activator Launcher, only a Java 6 or higher must be installed on your computer and 
the activator-laucher will do the rest:

    $ ./activator 'run-main com.example.SpringMain'
    
    $ ./activator 'run-main com.example.ScalikeMain'
    
    $ ./activator 'run-main com.example.SlickMain'

# The example
The example uses a JdbcConfig trait to configure the JdbcConnection. The H2JdbcConfig makes the configuration explicit
for the H2 database that runs in-memory. The ScalikeConnection or DbcpConnection uses the JdbcConfig to configure its
connection pool to provide datasources for the Jdbc library of choice. Slick uses its own database connection and 
abstraction.

# Conclusion
When you come from Java like me, the plain SQL to mapper way is really easy to understand, and familiar but not DRY. 
The spring-scala wrapper still has some issues. ScalikeJdbc is really good at what it does, and I will be coming back
to it soon. Slick is too slick for me, at least for now. 
    
Have fun!