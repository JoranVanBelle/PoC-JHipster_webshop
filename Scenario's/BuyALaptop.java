
import java.time.Duration;
import java.util.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import io.gatling.javaapi.jdbc.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
import static io.gatling.javaapi.jdbc.JdbcDsl.*;

public class BuyALaptop extends Simulation {

  {
    HttpProtocolBuilder httpProtocol = http
      .baseUrl("http://localhost:8080");

    Map<CharSequence, String> headers_0 = new HashMap<>();
    headers_0.put("Accept", "application/json, text/plain, */*");
    headers_0.put("Accept-Encoding", "gzip, deflate, br");
    headers_0.put("Accept-Language", "en-GB,en;q=0.9,nl-BE;q=0.8,nl;q=0.7,en-US;q=0.6");
    headers_0.put("Content-Type", "application/json");
    headers_0.put("DNT", "1");
    headers_0.put("Origin", "http://localhost:8080");
    headers_0.put("Sec-Fetch-Dest", "empty");
    headers_0.put("Sec-Fetch-Mode", "cors");
    headers_0.put("Sec-Fetch-Site", "same-origin");
    headers_0.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36");
    headers_0.put("sec-ch-ua", "Google Chrome\";v=\"111\", \"Not(A:Brand\";v=\"8\", \"Chromium\";v=\"111");
    headers_0.put("sec-ch-ua-mobile", "?0");
    headers_0.put("sec-ch-ua-platform", "Windows");

    Map<CharSequence, String> headers_1 = new HashMap<>();
    headers_1.put("Accept", "application/json, text/plain, */*");
    headers_1.put("Accept-Encoding", "gzip, deflate, br");
    headers_1.put("Accept-Language", "en-GB,en;q=0.9,nl-BE;q=0.8,nl;q=0.7,en-US;q=0.6");
    headers_1.put("DNT", "1");
    headers_1.put("Sec-Fetch-Dest", "empty");
    headers_1.put("Sec-Fetch-Mode", "cors");
    headers_1.put("Sec-Fetch-Site", "same-origin");
    headers_1.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36");
    headers_1.put("authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTY4MjQwNzE0Nn0.ggX5OkiDVZ9ouiSNsoTKNvF3j4F6dj35zZHI6cv4RG_9e2tMkT08eewLrdZ5ECBCbh2UU0eWOUq5rfsNq18pfQ");
    headers_1.put("sec-ch-ua", "Google Chrome\";v=\"111\", \"Not(A:Brand\";v=\"8\", \"Chromium\";v=\"111");
    headers_1.put("sec-ch-ua-mobile", "?0");
    headers_1.put("sec-ch-ua-platform", "Windows");

    Map<CharSequence, String> headers_25 = new HashMap<>();
    headers_25.put("Accept", "application/json, text/plain, */*");
    headers_25.put("Accept-Encoding", "gzip, deflate, br");
    headers_25.put("Accept-Language", "en-GB,en;q=0.9,nl-BE;q=0.8,nl;q=0.7,en-US;q=0.6");
    headers_25.put("Content-Type", "application/json");
    headers_25.put("DNT", "1");
    headers_25.put("Origin", "http://localhost:8080");
    headers_25.put("Sec-Fetch-Dest", "empty");
    headers_25.put("Sec-Fetch-Mode", "cors");
    headers_25.put("Sec-Fetch-Site", "same-origin");
    headers_25.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36");
    headers_25.put("authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTY4MjQwNzE0Nn0.ggX5OkiDVZ9ouiSNsoTKNvF3j4F6dj35zZHI6cv4RG_9e2tMkT08eewLrdZ5ECBCbh2UU0eWOUq5rfsNq18pfQ");
    headers_25.put("sec-ch-ua", "Google Chrome\";v=\"111\", \"Not(A:Brand\";v=\"8\", \"Chromium\";v=\"111");
    headers_25.put("sec-ch-ua-mobile", "?0");
    headers_25.put("sec-ch-ua-platform", "Windows");

    ScenarioBuilder scn = scenario("BuyALaptop")
      .exec(
        http("request_0")
          .post("/api/authenticate")
          .headers(headers_0)
          .body(RawFileBody("buyalaptop/0000_request.json"))
      )
      .pause(8)
      .exec(
        http("request_1")
          .get("/api/inventories/5")
              .headers(headers_1)
      )
      .pause(7)
      .exec(
        http("request_2")
          .get("/api/pricings/5")
              .headers(headers_1)
      )
      .pause(10)
      .pause(17)
      .exec(
        http("request_3")
          .post("/api/orders")
          .headers(headers_25)
          .body(RawFileBody("buyalaptop/0025_request.json"))
      );

	  setUp(scn.injectOpen(atOnceUsers(100),constantUsersPerSec(20).during(15))).protocols(httpProtocol);
  }
}
