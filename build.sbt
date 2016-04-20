name:="spark-log-streaming"
version:="1.0.0"
scalaVersion:="2.10.6"

libraryDependencies ++=Seq(
"org.apache.spark" %% "spark-core" % "1.3.0" % "provided",
"org.apache.spark" %% "spark-sql" % "1.3.0" ,
"org.apache.spark" %% "spark-hive" % "1.3.0",
"org.apache.spark" %% "spark-streaming" % "1.3.0"
)
