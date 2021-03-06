package org.richfaces.tests.metamer.ftest.richDataScroller;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.dataScroller.DataScroller.DataScrollerSwitchButton;
import org.richfaces.fragment.dataScroller.RichFacesDataScroller;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseForAllTests;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.BeforeMethod;

public class AbstractScrollerTest extends AbstractWebDriverTest {

    private final Attributes<DataScrollerAttributes> dataScrollerAttributes = getAttributes();

    private String tableText;

    @UseForAllTests(valuesFrom = FROM_FIELD, value = "ints")
    private Integer maxPages;
    private Integer[] ints = { 3, 4 };

    RichFacesDataScroller dataScroller;

    private void verifyBeforeScrolling() {
        tableText = dataTable.getText();
    }

    private void verifyAfterScrolling() {
        assertFalse(tableText.equals(dataTable.getText()));
        assertEquals(maxPages, Integer.valueOf(dataScroller.advanced().getCountOfVisiblePages()));
    }

    @FindByJQuery("table.rf-dt[id$=richDataTable]")
    private WebElement dataTable;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richDataScroller/simple.xhtml");
    }

    @BeforeMethod
    public void prepareComponent() {
        // dataScrollerAttributes.set(DataScrollerAttributes.fastStep, fastStep);
        dataScrollerAttributes.set(DataScrollerAttributes.maxPages, maxPages);
    }

    public void testNumberedPages(RichFacesDataScroller dataScroller) {
        this.dataScroller = dataScroller;

        // Get total pages count
        dataScroller.switchTo(DataScrollerSwitchButton.LAST);
        int totalPages = dataScroller.getActivePageNumber();

        // Create a list with all the pages
        List<Integer> allPages = new ArrayList<Integer>();
        for (int i = 1; i < totalPages; i++) {
            allPages.add(i);
        }

        // Randomly pick some list member, switch to that page, assert, and delete this page from list
        Random random = new Random();
        while (!allPages.isEmpty()) {
            int chosenOne = random.nextInt(allPages.size());
            verifyBeforeScrolling();
            dataScroller.switchTo(allPages.get(chosenOne));
            verifyAfterScrolling();
            allPages.remove(chosenOne);
        }
    }
}
