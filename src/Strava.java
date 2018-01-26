import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

class StravaActivity {
	private float distance;
	private long moving_time;
	private long elapsed_time;
	private String type;
	private LocalDateTime start_date_local;
	
	public StravaActivity() {
	}
	
	public LocalDate getDate() {
		return start_date_local.toLocalDate();
	}
	
	public String getType() {
		return type;
	}
	
	@Override
	public String toString() {
		LocalTime endTime = start_date_local.toLocalTime().plusSeconds(moving_time);
		return "act [type=" + type + ", distance=" + distance + ", moving=" + (moving_time/60) + ", elapsed=" + (elapsed_time/60)  + ", start=" + start_date_local.toLocalTime() + ".." + endTime + "]";
	}
}

public class Strava extends Downloader {
    private static final String SITE = "https://www.strava.com/api/v3/athlete/activities";
    
    public Strava() {
    }

    @Override
    protected void makeUrl(UriBuilder uri) {    	
    	uri.setHost(SITE);
    	uri.addParam("page",         "1");
    	uri.addParam("access_token", Config.getStravaToken());
    }

	@Override
	protected void fillRequest(HttpURLConnection con) {
		con.setRequestProperty("Content-Type", "application/json");	
	}			   

	public void parse(InputStream stream) {
		GsonBuilder builder = new GsonBuilder();

		// to deserialize LocalDateTime class
		//
		builder.registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
			@Override
			public LocalDateTime deserialize(JsonElement json, Type tpe, JsonDeserializationContext context)
					throws JsonParseException {
				return ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).toLocalDateTime();
				//Instant instant = Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong());
		        //return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());				
			}
		});
		
		Gson gson = builder.create();
		
		StravaActivity[] acts = gson.fromJson(new InputStreamReader(stream), StravaActivity[].class);
		Map<LocalDate, List<StravaActivity>> items = Arrays.stream(acts)
				.filter(a -> a.getType().equals("NordicSki") == false)
				.collect(Collectors.groupingBy(StravaActivity::getDate));
		for (LocalDate d : items.keySet()) {
			System.out.println(d);
			for (StravaActivity a : items.get(d)) {
				System.out.println("  " + a);
			}
		}
	}
}
