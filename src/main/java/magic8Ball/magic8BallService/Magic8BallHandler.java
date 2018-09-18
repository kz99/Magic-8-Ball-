package magic8Ball.magic8BallService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/")
public class Magic8BallHandler {
	
	private static final Logger LOGGER = Logger.getLogger(Magic8BallHandler.class.getName());  
    /**
     * The QUERY_PARAM_QUESTION.
     */
    public static final String QUERY_PARAM_QUESTION = "question";
    
    /**
     * The QUERY_PARAM_USER.
     */
    public static final String QUERY_PARAM_USER = "user";
    
    /**
     * The DEFAULT_USER.
     */
    public static final String DEFAULT_USER = "anonymous";
    
    /**
     * The AUTHORIZATION_HEADER_REQUEST.
     */
    public static final String AUTHORIZATION_HEADER_REQUEST = "authorization";

    /**
     * The AUTHORIZATION_HEADER_RESPONSE.
     */
    public static final String AUTHORIZATION_HEADER_RESPONSE = "WWW-Authenticate";
    
    /**
     * The AUTHORIZATION_METHOD_BASIC.
     */
    public static final String AUTHORIZATION_METHOD_BASIC = "Basic";
    
    /**
     * The DATE_FORMAT.
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * The NEWLINE.
     */
    public static final String NEWLINE = "\n"; // Use good old Unix newlines!

    /**
     * The magic8Answers.
     */
    private static final String[] magic8Answers = {"It is certain", "It is decidedly so" ,"Without a doubt",
        "Yes definitely","You may rely on it","As I see it, yes","Most likely","Outlook good","Yes",
        "Signs point to yes","Reply hazy try again","Ask again later","Better not tell you now",
        "Cannot predict now","Concentrate and ask again","Don't count on it","My reply is no",
        "My sources say no","Outlook not so good","Very doubtful"};
    
    /**
     * @return a random answer from the answer list {@code magic8Answers}
     */
    protected String getMagic8Answer() {
        int rand = ThreadLocalRandom.current().nextInt(20);
        return magic8Answers[rand];
    }

    /**
     * Gets the magic-8 answer in the format specified by the Accept header
     * @returns magic-8 answer in the specified format
     */
    @GET
    @Path("/answer")
    @ApiOperation(value="Obtains plain-text magic-8 answer", notes="If magic-8 answer 'unused' is returned", response=String.class)
    @Produces({MediaType.TEXT_PLAIN, MediaType.TEXT_HTML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
    public Response magic8AnswerV1(@Context HttpHeaders headers, 
        @ApiParam(value = "the question you asked") @QueryParam(QUERY_PARAM_QUESTION) String question) {
        String productionType = headers.getRequestHeader("Accept").get(0);
        String responseString = "";      
        
        /* Get magic8Answer */
        String magic8Answer = getMagic8Answer();
        String timestamp = new SimpleDateFormat(DATE_FORMAT).format(new Date());
        /* Media types in order of preference */
        if (productionType.contains(MediaType.TEXT_HTML)) {
            responseString =  htmlAnswerV1(magic8Answer, question, timestamp);
        } else if (productionType.contains(MediaType.TEXT_PLAIN)) {
            responseString = plainTextAnswerV1(magic8Answer, question, timestamp);
        } else if (productionType.contains(MediaType.TEXT_XML)) {
            responseString = xmlAnswerV1(magic8Answer, question, timestamp); 
        } else if (productionType.contains(MediaType.APPLICATION_JSON)) {
            responseString =  jsonAnswerV1(magic8Answer, question, timestamp);
        } else {
            String errorMessage = String.format("Unrecognized media type requested: %s.  Should not be possible", productionType);
            LOGGER.severe(errorMessage);
            return Response.status(500).entity(errorMessage).build();
        }
        return Response.status(200).entity(responseString).build();
    }
    
    /**
     * Gets the text magic-8 answer
     * @returns text magic-8 answer
     */
    public String plainTextAnswerV1(String magic8Answer, String question, String timestamp) {
        StringBuilder result = new StringBuilder();
        result.append("response: " + magic8Answer);
        
        if (question != null && !question.isEmpty())
            result.append(NEWLINE + "question: " + question);
        
        result.append(NEWLINE + "timestamp: " + timestamp);
        return result.toString();
    }


    /**
     * Gets the xml magic-8 answer
     * @returns xml magic-8 answer
     */
    public String xmlAnswerV1(String magic8Answer, String question, String timestamp) {
        StringBuilder result = new StringBuilder();
        result.append("<?xml version=\"1.0\"?>");
        result.append( "<response> " + magic8Answer + "</response>");
        
        if (question != null && !question.isEmpty())
            result.append("<question> " + question + "</question>");
            
        result.append("<timestamp> " + timestamp + "</timestamp>");
        return result.toString();
    }


    /**
     * Gets the HTML magic-8 answer
     * @returns HTML magic-8 answer
     */
    public String htmlAnswerV1(String magic8Answer, String question, String timestamp) {
        StringBuilder result = new StringBuilder();
        result.append( "<html> " + "<title>" + magic8Answer + "</title>");
        result.append("<body><h2>response: " + magic8Answer + "</h2>");
        
        if (question != null && !question.isEmpty())
            result.append("<h3>question: " + question + "</h3>");

        result.append("<h3>timestamp " + timestamp + "</h3>");
        
        result.append("</body></html>");
        
        return result.toString();
    }


    /**
     * Gets the JSON magic-8 answer
     * @returns JSON magic-8 answer
     */
    public String jsonAnswerV1(String magic8Answer, String question, String timestamp) {
        StringBuilder result = new StringBuilder();
        result.append("{ \"response\" : \"" + magic8Answer + "\",");
        
        if (question != null && !question.isEmpty())
            result.append("\"question\" : \"" + question + "\",");
            
        result.append("\"timestamp\" : \"" + timestamp + "\"");
        
        result.append("}");
        return result.toString();
    }
}