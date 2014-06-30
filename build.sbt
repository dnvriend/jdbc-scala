name := "jdbc-scala"

version := "1.0.0"

scalaVersion := "2.10.4"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers += "spring-snapshots" at "http://repo.springsource.org/snapshot"

libraryDependencies ++= Seq(
  "com.h2database" % "h2" % "1.4.179",
  "commons-dbcp" % "commons-dbcp"% "1.4",
  "com.typesafe.slick" %% "slick" % "2.0.2",
  "org.scalikejdbc" %% "scalikejdbc"       % "2.0.4",
  "org.springframework.scala" %% "spring-scala" % "1.0.0.BUILD-SNAPSHOT",
  "org.springframework" % "spring-jdbc" % "4.0.5.RELEASE",
  "org.slf4j" % "slf4j-nop" % "1.6.4"
)

com.github.retronym.SbtOneJar.oneJarSettings