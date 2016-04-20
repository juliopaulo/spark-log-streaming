

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Duration
import org.apache.spark.sql.SaveMode

object StreamingAnalizer {
  
  val WINDOW_LENGTH=new Duration(10*1000)
  val SLIDE_INTERVAL=new Duration(10*1000)
  
  def main(args: Array[String]): Unit = {
    
    val conf=new SparkConf().setAppName("streaming").setMaster("local[4]")
    
    val sc=new SparkContext(conf)
    
    val sqlContext=new SQLContext(sc)
    
    import sqlContext.implicits._
    
    val streamingContext=new StreamingContext(sc,SLIDE_INTERVAL)
    
    val logStreaming=streamingContext.textFileStream("file:///var/log/hue/access.log")
    
    val accessLogDF=logStreaming.map ( AccessLog.parseLine ).persist()
    
    val windowDStream=accessLogDF.window(WINDOW_LENGTH,SLIDE_INTERVAL)
    
    windowDStream.foreachRDD(accessLogs => 
      {
        if(accessLogs.count()==0){
          println("no se ha recibido logs en este intervalos")
        }
        else
        {
         accessLogs.toDF().registerTempTable("logs") 
         
         val responseCodeCount= sqlContext.sql(" SELECT dateTime,method,endPoint,protocol FROM logs ")
         
         responseCodeCount.save("hdfs:///user/cloudera/streaming/hue/", SaveMode.Overwrite)
         
         responseCodeCount.collect().foreach(println)
         
        }
      
      }
    )
    
    streamingContext.start()
    streamingContext.awaitTermination()
  }
}
