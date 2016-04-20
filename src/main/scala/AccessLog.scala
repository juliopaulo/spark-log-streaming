


case class CaseAccessLog(
  dateTime:String,
  levelLog:String,
  ipAddres:String,
  userId:String,  
  clientIdent:String,
  method:String,
  endPoint:String,
  protocol:String
  ){
  
}



object AccessLog {
  val PATTERN="""^\[([\w:/]+\s[\w:]+\s[+\-]\d{4})\] (\S+)     (\S+) (\S+) (\S+) "(\S+) (\S+) (\S+)"""".r
  
  def parseLine(log :String):CaseAccessLog={    
    
    val result=PATTERN.findFirstMatchIn(log)
    
    if(result.isEmpty){
//      throw new RuntimeException("No se puede parser el log: "+log)
      return CaseAccessLog("","","","","","","","")
    }
    
    val m=result.get
    
    return CaseAccessLog(
              m.group(1),
              m.group(2),
              m.group(3),
              m.group(4),
              m.group(5),
              m.group(6),
              m.group(7),
              m.group(8))
   
  }
}
