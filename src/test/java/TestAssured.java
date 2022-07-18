import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@RunWith(DataProviderRunner.class)

public class TestAssured {

    public static RequestSpecification requestSpecification;

    @DataProvider
    public static Object[][] codesAndPlaces() {
        return new Object[][]{
                {"us", "90210", "Beverly Hills", "California"},
                {"us", "12345", "Schenectady", "New York"},
                {"ca", "B2R", "Waverley", "Nova Scotia"},
        };
    }

    @BeforeClass
    public static void specification(){
        requestSpecification = new RequestSpecBuilder().setBaseUri("http://api.zippopotam.us").build();
    }


    @Test
    @UseDataProvider("codesAndPlaces")
    public void TestForResponse(String cCode, String sCode, String cName, String sName) {
        given().pathParam("cCode", cCode).pathParam("sCode", sCode).when().get("{cCode}/{sCode}").then().assertThat().statusCode(200);
    }

    @Test
    @UseDataProvider("codesAndPlaces")
    public void TestForContentType(String cCode, String sCode, String cName, String sName) {
        given().pathParam("cCode", cCode).pathParam("sCode", sCode).when().get("{cCode}/{sCode}").then().assertThat().contentType(ContentType.JSON);
    }

    @Test
    @UseDataProvider("codesAndPlaces")
    public void TestForLog(String cCode, String sCode, String cName, String sName) {
        given().log().all().pathParam("cCode", cCode).pathParam("sCode", sCode).when().get("http://zippopotam.us/{cCode}/{sCode}").then().log().body();
    }

    @Test
    @UseDataProvider("codesAndPlaces")
    public void TestForBodyPart1(String cCode, String sCode, String cName, String sName) {
        given().log().all().pathParam("cCode", cCode).pathParam("sCode", sCode).when().get("http://zippopotam.us/{cCode}/{sCode}").then().assertThat().body("places[0].'place name'", equalTo(cName));
    }

    @Test
    @UseDataProvider("codesAndPlaces")
    public void TestForBodyPart2(String cCode, String sCode, String cName, String sName) {
        given().log().all().pathParam("cCode", cCode).pathParam("sCode", sCode).when().get("http://zippopotam.us/{cCode}/{sCode}").then().assertThat().body("places[0].state", equalTo(sName));
    }

    @Test
    @UseDataProvider("codesAndPlaces")
    public void TestForBodyPart3(String cCode, String sCode, String cName, String sName) {
        given().log().all().pathParam("cCode", cCode).pathParam("sCode", sCode).when().get("http://zippopotam.us/{cCode}/{sCode}").then().assertThat().body("places.'place name'", hasItem(cName));
    }

    @Test
    @UseDataProvider("codesAndPlaces")
    public void TestForBodyPart4(String cCode, String sCode, String cName, String sName) {
        given().log().all().pathParam("cCode", cCode).pathParam("sCode", sCode).when().get("http://zippopotam.us/{cCode}/{sCode}").then().assertThat().body("places.'place name'", hasSize(1));
    }

}
