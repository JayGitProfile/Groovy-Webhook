import groovy.json.JsonSlurper
import org.apache.commons.io.IOUtils

def urlRoot = ""
def issueId = ""
def userName = ""
def password = ""
def address = "${urlRoot}/rest/api/2/issue/${issueId}"
def authString = "${userName}:${password}".getBytes().encodeBase64().toString()
//connection string
print "Integration API: ${address}"
def conn = address.toURL().openConnection()
//sets the encoded authString to Authorization object
if(authString.length() > 0)
{
   conn.setRequestProperty( "Authorization", "Basic ${authString}" )
}
if( conn.responseCode == 200 ) 
{
    println "Connection successful"
    //resonse is in json format
    def jsonString = IOUtils.toString(conn.inputStream)
    def issueRest = new JsonSlurper().parseText(jsonString)
    print "Issue key: ${issueRest.key}"
    print "Issue status: ${issueRest.fields.status.name}"
    print "Issue summary: ${issueRest.fields.summary}"
    //api to fetch description we use another api
    def descriptionAPI = "${address}?fields=description"
    print "Description Api: ${address}?fields=description"
    def authString2 = "${userName}:${password}".getBytes().encodeBase64().toString()
    def conn2 = descriptionAPI.toURL().openConnection()
    if(authString2.length() > 0)
    {
        conn2.setRequestProperty( "Authorization", "Basic ${authString2}" )
    }
    if(conn2.responseCode == 200)
    {
        print "Description API connection successful"
        def jsonString2 = IOUtils.toString(conn2.inputStream)
        def issueRest2 = new JsonSlurper().parseText(jsonString2)
        print "Issue description: ${issueRest2.fields.description}"
    }
    else
    {
        print "Description API connection failed"
    }
}
else 
{
  println "No connection established"
}