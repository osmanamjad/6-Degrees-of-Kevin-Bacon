package ca.utoronto.utm.mcs;

import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
public class GetActor implements HttpHandler {

	public void handle(HttpExchange r) throws IOException {
        try {
            if (r.getRequestMethod().equals("GET")) {
                handleGet(r);
            } else {
            	throw new BadRequestException("Invalid Request");
            }
        } catch (BadRequestException e1) {
			r.sendResponseHeaders(400, -1);
        } catch (JSONException e2) {
			r.sendResponseHeaders(400, -1);
        } catch (NotFoundException e3) {
			r.sendResponseHeaders(404, -1);
		} catch (Exception e4) {
			r.sendResponseHeaders(500, -1);
		}
	}

	private void handleGet(HttpExchange r) throws Exception, NotFoundException, BadRequestException, IOException, JSONException {
		String body = Utils.convert(r.getRequestBody());
        JSONObject deserialized = new JSONObject(body);

        String actorId = "";
        
        if (deserialized.has("actorId"))
            actorId = deserialized.getString("actorId");
        else
        	throw new JSONException("Missing information in JSON");
        
        Neo4jDB nDB = new Neo4jDB();
        
        String actorJSON = nDB.getActor(actorId);
        
        r.sendResponseHeaders(200, actorJSON.length());
        OutputStream os = r.getResponseBody();
        os.write(actorJSON.getBytes());
        os.close();
	}

}
