
import java.time.Duration;
import java.util.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import io.gatling.javaapi.jdbc.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
import static io.gatling.javaapi.jdbc.JdbcDsl.*;

public class LookForArticleLessUsersSomeoneBuysLaptop extends Simulation {

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
    headers_1.put("authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTY4MjQwODM2Mn0.Z3jcxp6xb-2eXX6OL8K61risbqYf8Mk0bCc5ph1Asib_L2C-ZKD8gG_Fww9a5QgouNnjiA89s_xX7nsDNXNkYw");
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
    headers_25.put("authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTY4MjQwODM2Mn0.Z3jcxp6xb-2eXX6OL8K61risbqYf8Mk0bCc5ph1Asib_L2C-ZKD8gG_Fww9a5QgouNnjiA89s_xX7nsDNXNkYw");
    headers_25.put("sec-ch-ua", "Google Chrome\";v=\"111\", \"Not(A:Brand\";v=\"8\", \"Chromium\";v=\"111");
    headers_25.put("sec-ch-ua-mobile", "?0");
    headers_25.put("sec-ch-ua-platform", "Windows");

    ScenarioBuilder scn1 = scenario("LookForAnArticle1")
      .exec(
        http("request_0")
          .post("/api/authenticate")
          .headers(headers_0)
          .body(RawFileBody("lookforarticlelessuserssomeonebuyslaptop/0000_request.json"))
      )
      .pause(9)
      .exec(
        http("request_1")
          .get("/api/inventories/1")
              .headers(headers_1)
      )
      .pause(1)
      .exec(
        http("request_2")
          .get("/api/pricings/1")
              .headers(headers_1)
      );

ScenarioBuilder scn2 = scenario("LookForAnArticle2")
      .exec(
        http("request_0")
          .post("/api/authenticate")
          .headers(headers_0)
          .body(RawFileBody("lookforarticlelessuserssomeonebuyslaptop/0000_request.json"))
      )
      .pause(9)
      .exec(
        http("request_1")
          .get("/api/inventories/2")
              .headers(headers_1)
      )
      .pause(1)
      .exec(
        http("request_2")
          .get("/api/pricings/2")
              .headers(headers_1)
      );


ScenarioBuilder scn3 = scenario("LookForAnArticle3")
      .exec(
        http("request_0")
          .post("/api/authenticate")
          .headers(headers_0)
          .body(RawFileBody("lookforarticlelessuserssomeonebuyslaptop/0000_request.json"))
      )
      .pause(9)
      .exec(
        http("request_1")
          .get("/api/inventories/3")
              .headers(headers_1)
      )
      .pause(1)
      .exec(
        http("request_2")
          .get("/api/pricings/3")
              .headers(headers_1)
      );


 ScenarioBuilder scn4 = scenario("LookForAnArticle4")
      .exec(
        http("request_0")
          .post("/api/authenticate")
          .headers(headers_0)
          .body(RawFileBody("lookforarticlelessuserssomeonebuyslaptop/0000_request.json"))
      )
      .pause(9)
      .exec(
        http("request_1")
          .get("/api/inventories/4")
              .headers(headers_1)
      )
      .pause(1)
      .exec(
        http("request_2")
          .get("/api/pricings/4")
              .headers(headers_1)
      );

ScenarioBuilder scn5 = scenario("LookForAnArticle5")
      .exec(
        http("request_0")
          .post("/api/authenticate")
          .headers(headers_0)
          .body(RawFileBody("lookforarticlelessuserssomeonebuyslaptop/0000_request.json"))
      )
      .pause(9)
      .exec(
        http("request_1")
          .get("/api/inventories/5")
              .headers(headers_1)
      )
      .pause(1)
      .exec(
        http("request_2")
          .get("/api/pricings/5")
              .headers(headers_1)
      );

ScenarioBuilder scn6 = scenario("LookForAnArticle6")
      .exec(
        http("request_0")
          .post("/api/authenticate")
          .headers(headers_0)
          .body(RawFileBody("lookforarticlelessuserssomeonebuyslaptop/0000_request.json"))
      )
      .pause(9)
      .exec(
        http("request_1")
          .get("/api/inventories/6")
              .headers(headers_1)
      )
      .pause(1)
      .exec(
        http("request_2")
          .get("/api/pricings/6")
              .headers(headers_1)
      );

ScenarioBuilder scn7 = scenario("LookForAnArticle7")
      .exec(
        http("request_0")
          .post("/api/authenticate")
          .headers(headers_0)
          .body(RawFileBody("lookforarticlelessuserssomeonebuyslaptop/0000_request.json"))
      )
      .pause(9)
      .exec(
        http("request_1")
          .get("/api/inventories/7")
              .headers(headers_1)
      )
      .pause(1)
      .exec(
        http("request_2")
          .get("/api/pricings/7")
              .headers(headers_1)
      );

ScenarioBuilder scn8 = scenario("LookForAnArticle8")
      .exec(
        http("request_0")
          .post("/api/authenticate")
          .headers(headers_0)
          .body(RawFileBody("lookforarticlelessuserssomeonebuyslaptop/0000_request.json"))
      )
      .pause(9)
      .exec(
        http("request_1")
          .get("/api/inventories/8")
              .headers(headers_1)
      )
      .pause(1)
      .exec(
        http("request_2")
          .get("/api/pricings/8")
              .headers(headers_1)
      );

ScenarioBuilder scn9 = scenario("LookForAnArticle9")
      .exec(
        http("request_0")
          .post("/api/authenticate")
          .headers(headers_0)
          .body(RawFileBody("lookforarticlelessuserssomeonebuyslaptop/0000_request.json"))
      )
      .pause(9)
      .exec(
        http("request_1")
          .get("/api/inventories/9")
              .headers(headers_1)
      )
      .pause(1)
      .exec(
        http("request_2")
          .get("/api/pricings/9")
              .headers(headers_1)
      );

ScenarioBuilder scn10 = scenario("LookForAnArticle10")
      .exec(
        http("request_0")
          .post("/api/authenticate")
          .headers(headers_0)
          .body(RawFileBody("lookforarticlelessuserssomeonebuyslaptop/0000_request.json"))
      )
      .pause(9)
      .exec(
        http("request_1")
          .get("/api/inventories/10")
              .headers(headers_1)
      )
      .pause(1)
      .exec(
        http("request_2")
          .get("/api/pricings/10")
              .headers(headers_1)
      );
      
ScenarioBuilder scn11 = scenario("BuyALaptop")
         .exec(
        http("request_0")
          .post("/api/authenticate")
          .headers(headers_0)
          .body(RawFileBody("lookforarticlelessuserssomeonebuyslaptop/0000_request.json"))
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
          .body(RawFileBody("lookforarticlelessuserssomeonebuyslaptop/0025_request.json"))
      );

ScenarioBuilder scn12 = scenario("BuyALaptop2")
      .exec(
        http("request_0")
          .post("/api/authenticate")
          .headers(headers_0)
          .body(RawFileBody("lookforarticlelessuserssomeonebuyslaptop/0000_request.json"))
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
          .body(RawFileBody("lookforarticlelessuserssomeonebuyslaptop/0025_request.json"))
      );

      setUp(
        scn1.injectOpen(
        nothingFor(4),
        atOnceUsers(5),
        rampUsersPerSec(1).to(5).during(300)
        ),
        scn2.injectOpen(
        nothingFor(4),
        atOnceUsers(6),
        rampUsersPerSec(1).to(6).during(350)
        ),
        scn3.injectOpen(
        nothingFor(4),
        atOnceUsers(2),
        rampUsersPerSec(1).to(3).during(150)
        ),
        scn4.injectOpen(
        nothingFor(4),
        atOnceUsers(1),
        rampUsersPerSec(1).to(5).during(250)
        ),
        scn5.injectOpen(
        nothingFor(5),
        atOnceUsers(2),
        rampUsersPerSec(0).to(1).during(225)
        ),
        scn6.injectOpen(
        nothingFor(2),
        atOnceUsers(3),
        rampUsersPerSec(1).to(3).during(360)
        ),
        scn7.injectOpen(
        nothingFor(9),
        atOnceUsers(3),
        rampUsersPerSec(1).to(4).during(120)
        ),
        scn8.injectOpen(
        nothingFor(0),
        atOnceUsers(20),
        rampUsersPerSec(1).to(4).during(140)
        ),
        scn9.injectOpen(
        nothingFor(2),
        atOnceUsers(20),
        rampUsersPerSec(0).to(6).during(180)
        ),
        scn10.injectOpen(
        nothingFor(4),
        atOnceUsers(10),
        rampUsersPerSec(1).to(3).during(450)
        ),
        scn11.injectOpen(
          nothingFor(15),
          atOnceUsers(100)
        ),
        scn12.injectOpen(
          nothingFor(120),
          atOnceUsers(100)
        )
        ).protocols(httpProtocol);

  }
}
