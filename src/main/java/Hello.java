package main.java;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.aylien.newsapi.ApiClient;
import com.aylien.newsapi.ApiException;
import com.aylien.newsapi.Configuration;
import com.aylien.newsapi.api.DefaultApi;
import com.aylien.newsapi.auth.ApiKeyAuth;
import com.aylien.newsapi.models.Stories;
import com.aylien.newsapi.models.Story;
import com.aylien.newsapi.parameters.StoriesParams;

@RestController
@EnableAutoConfiguration
public class Hello {

    @RequestMapping("/news")
    @CrossOrigin(origins = "*")
    public String getNews() {
    	List<String> list = new ArrayList<String>();
    	//System.setProperty("https.proxyHost", "squid01.hyd.int.untd.com");
    	//System.setProperty("https.proxyPort", "3128");
    	final String uri = "https://newsapi.org/v1/articles?source=bbc-news&sortBy=top&apiKey=b4fbc381c9ee43c495202b5d6ed4c14e";
    	
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        
    	list = getAylienNews();
        return list.toString();
    }
    
    @RequestMapping("/")
    public ModelAndView index() {
    	ModelAndView hello = new ModelAndView("Hello.html");
        return hello;
    }
    
    public List getAylienNews(){
    	ApiClient defaultClient = Configuration.getDefaultApiClient();

        // Configure API key authorization: app_id
        ApiKeyAuth app_id = (ApiKeyAuth) defaultClient.getAuthentication("app_id");
        app_id.setApiKey("c542063c");

        // Configure API key authorization: app_key
        ApiKeyAuth app_key = (ApiKeyAuth) defaultClient.getAuthentication("app_key");
        app_key.setApiKey("68da79113a3f01d37297addc974b3a28");

        DefaultApi apiInstance = new DefaultApi();

        StoriesParams.Builder storiesBuilder = StoriesParams.newBuilder();
        List<String> sourceName = Arrays.asList("The New York Times", "The Washington Post", "Wall Street Journal");
        storiesBuilder.setTitle("India");
        storiesBuilder.setSortBy("published_at");
        storiesBuilder.setSourceName(sourceName);
        storiesBuilder.setLanguage(Arrays.asList("en"));
        storiesBuilder.setPublishedAtStart("NOW-7DAYS");
        storiesBuilder.setPublishedAtEnd("NOW");

        List<String> list = new ArrayList<String>();
        StringBuilder newsJson = new StringBuilder();
        try {
            Stories result = apiInstance.listStories(storiesBuilder.build());
            for (Iterator<Story> i = result.getStories().iterator(); i.hasNext();){
                Story story = i.next();
                newsJson.append("{");
                newsJson.append("\"title\":\""+story.getTitle()+"\",");
                newsJson.append("\"image_url\":\""+story.getMedia().get(0).getUrl()+"\",");
                newsJson.append("\"summary\":\""+story.getSummary().getSentences().toString()+"\",");
                newsJson.append("\"source\":\""+story.getSource().getName()+"\"");
                if(i.hasNext())
                	newsJson.append("},");
                else
                	newsJson.append("}");
            }
        } catch (ApiException e) {
            System.err.println("Exception when calling DefaultApi#listStories");
            e.printStackTrace();
        }
        String finalJson = newsJson.toString();
        list.add(finalJson);
        return list;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Hello.class, args);
    }

}
