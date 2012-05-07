/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2012, Red Hat, Inc.
 * and individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 * *****************************************************************************
 */
package org.richfaces.tests.metamer.ftest.richFileUpload;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.fileUploadAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import org.jboss.test.selenium.support.ui.ElementDisplayed;
import org.jboss.test.selenium.support.ui.ElementNotDisplayed;
import org.jboss.test.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRichFileUploadSe2 extends AbstractWebDriverTest {

    private static final String uploadButton = "span.rf-fu-btn-upl";
    private static final String clearAllButton = "span.rf-fu-btn-clr";
    private static final String itemClear = "div.rf-fu-itm .rf-fu-itm-rgh a";
    private static final String notAcceptableFile = "file1.x";
    private static final String acceptableFile = "file1.txt";
    private static final String bigFile = "bigFile.txt";
    private static final String[] filenames = { acceptableFile, "file2.txt", bigFile, notAcceptableFile };
    private static final String ap = "\"";
    private FileUploadPage page = new FileUploadPage();
    private int filesToUploadCount;
    private int filesUploadedCount;
//    private String fileInputFieldXPath = "//input[@type='file'][@class='rf-fu-inp']";
//    private WebElement fileInputField;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richFileUpload/simple.xhtml");
    }

    @BeforeMethod
    public void pageLoad() {
        injectWebElementsToPage(page);
        filesToUploadCount = 0;
        filesUploadedCount = 0;
    }

    private boolean isElementPresent(String css) {
        try {
            driver.findElement(By.cssSelector(css));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isElementVisible(String css) {
        try {
            return driver.findElement(By.cssSelector(css)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Sends file to fileupload input field and waits for page update.
     *
     * @param filename
     * @param willBeAccepted
     */
    private void sendFileToInput(String filename, boolean willBeAccepted) {
        File file = null;
        try {
            file = new File(TestRichFileUploadSe2.class.getResource(filename).toURI());
        } catch (URISyntaxException ex) {
        }
        assertTrue(file != null, "File does not exist.");
        assertTrue(file.exists(), "File does not exist.");

//        String actXPath = fileInputFieldXPath + "[" + (filesToUploadCount + 1) + "]";
//        new WebDriverWait(driver).failWith("Cannot find file input field.").
//                until(ElementPresent.getInstance().element(driver.findElement(By.xpath(actXPath))));
//        fileInputField = driver.findElement(By.xpath(actXPath));
//        fileInputField.sendKeys(file.getAbsolutePath());
        page.fileInputField.sendKeys(file.getAbsolutePath());
        if (willBeAccepted) {
            this.filesToUploadCount++;
        }
        waitUntilFilesToUploadListShow(filesToUploadCount);
    }

    /**
     * Sends file to fileupload input field and waits for page update. Then
     * clicks on submit button to upload files and waits for page update.
     *
     * @param filename
     * @param willBeAccepted
     * @param willBeUploaded
     */
    private void sendFile(String filename, boolean willBeAccepted, boolean willBeUploaded) {
        sendFileToInput(filename, willBeAccepted);
        page.uploadButton.click();
        if (willBeUploaded) {
            filesUploadedCount++;
        }
        waitUntilUploadedFilesListShow(filesUploadedCount);
    }

    private void waitUntilUploadedFilesListShow(int expectedNumberOfFiles) {
        waitForPageToLoad();
        if (expectedNumberOfFiles == 0) {
        } else {
            new WebDriverWait(driver).failWith("No uploaded files list shown.").
                    until(ElementDisplayed.getInstance().
                    element(driver.findElement(
                    By.xpath("//div[@class='rf-fu-itm'][" + expectedNumberOfFiles + "]"
                    + "//span[@class='rf-fu-itm-st']"))));
        }
    }

    private void waitUntilFilesToUploadListShow(int expectedNumberOfFiles) {
        waitForPageToLoad();
        if (expectedNumberOfFiles == 0) {
        } else {
            new WebDriverWait(driver).failWith("No files to upload list shown.").
                    until(ElementDisplayed.getInstance().
                    element(driver.findElement(
                    By.xpath("//div[@class='rf-fu-itm'][" + expectedNumberOfFiles + "]"))));
        }
    }

    @Test
    @Templates(exclude = { "richExtendedDataTable", "richCollapsibleSubTable" })
    public void testSingleFileUpload() {
        sendFileToInput(filenames[0], true);

        waitUntilFilesToUploadListShow(1);
        List<WebElement> filesToUpload = page.itemsToUpload;
        assertTrue(filesToUpload.size() == 1, "File not loaded");
        assertTrue(filesToUpload.get(0).getText().equals(filenames[0]), "Label with filename does not appear.");
        assertTrue(isElementPresent(itemClear), "Clear button does not appear.");
        assertTrue(isElementVisible(uploadButton), "Upload button should be on the page.");
        assertTrue(isElementVisible(clearAllButton), "Clear all button should be on the page.");

        page.uploadButton.click();

        waitUntilUploadedFilesListShow(filesToUploadCount);
        List<WebElement> uploadedFiles = page.uploadedFilesList;
        assertTrue(uploadedFiles.size() == 1, "List of uploaded files should contain one file.");
        assertTrue(uploadedFiles.get(0).getText().equals(filenames[0]),
                "Uploaded file does not appear in uploadedList.");
    }

    @Test(groups = "4.Future")
    @Templates("richExtendedDataTable")
    @IssueTracking("https://issues.jboss.org/browse/RF-12122")
    public void testSingleFileUploadInEDT() {
        testSingleFileUpload();
    }

    @Test(groups = "4.Future")
    @Templates("richCollapsibleSubTable")
    @IssueTracking("https://issues.jboss.org/browse/RF-12122")
    public void testSingleFileUploadInCST() {
        testSingleFileUpload();
    }

    @Test
    public void testAcceptedTypes() {
        String acceptable = "txt";
        fileUploadAttributes.set(FileUploadAttributes.acceptedTypes, acceptable);

        sendFileToInput(notAcceptableFile, false);

        waitUntilFilesToUploadListShow(0);
        List<WebElement> filesToUpload = page.itemsToUpload;
        assertTrue(filesToUpload.isEmpty(), "AcceptedType does not work.");

        sendFile(acceptableFile, true, true);

        filesToUpload = page.itemsToUpload;
        assertFalse(filesToUpload.isEmpty(), "AcceptedType does not work.");
    }

    @IssueTracking(value = "https://issues.jboss.org/browse/RF-12039")
    @Test(groups = "4.Future")
    public void testData() {
        String testData = "Richfaces 4";
        fileUploadAttributes.set(FileUploadAttributes.data, testData);
        fileUploadAttributes.set(FileUploadAttributes.oncomplete, "data = event.data");

        executeJS("window.data = \"\";");

        sendFile(filenames[0], true, true);

        String data = (String) executeJS("return window.data");
        assertEquals(data, testData);
    }

    @Test
    @Templates(exclude = { "richExtendedDataTable", "richCollapsibleSubTable" })
    public void testDoneLabel() {
        String doneLabel = "Done and done";
        fileUploadAttributes.set(FileUploadAttributes.doneLabel, doneLabel);

        sendFile(filenames[0], true, true);

        String readDoneLabel = page.uploadStatusLabel.getText();
        assertEquals(readDoneLabel, doneLabel);
    }

    @Test(groups = "4.Future")
    @Templates("richExtendedDataTable")
    @IssueTracking("https://issues.jboss.org/browse/RF-12122")
    public void testDoneLabelInEDT() {
        testDoneLabel();
    }

    @Test(groups = "4.Future")
    @Templates("richCollapsibleSubTable")
    @IssueTracking("https://issues.jboss.org/browse/RF-12122")
    public void testDoneLabelInCST() {
        testDoneLabel();
    }

    @Test
    public void testOnFileSubmit() {
        fileUploadAttributes.set(FileUploadAttributes.onfilesubmit, "metamerEvents += \"filesubmit \"");

        executeJS("window.metamerEvents = \"\";");

        sendFile(filenames[0], true, true);

        String event = (String) executeJS("return window.metamerEvents");
        event = event.trim();

        assertEquals(event, "filesubmit", "Attribute onfilesubmit doesn't work");
    }

    @Test
    public void testOnUploadcomplete() {
        fileUploadAttributes.set(FileUploadAttributes.onuploadcomplete, "metamerEvents += \"uploadcomplete \"");

        executeJS("window.metamerEvents = \"\";");

        sendFile(filenames[0], true, true);

        String event = (String) executeJS("return window.metamerEvents");
        event = event.trim();

        assertEquals(event, "uploadcomplete", "Attribute onuploadcomplete doesn't work.");
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12037")
    @Test(groups = "4.Future")
    public void testEvents() {
        fileUploadAttributes.set(FileUploadAttributes.onbeforedomupdate, "metamerEvents += \"beforedomupdate \"");
        fileUploadAttributes.set(FileUploadAttributes.onbegin, "metamerEvents += \"begin \"");
        fileUploadAttributes.set(FileUploadAttributes.oncomplete, "metamerEvents += \"complete \"");

        executeJS("window.metamerEvents = \"\";");

        sendFile(filenames[0], true, true);

        String event = (String) executeJS("return window.metamerEvents");
        String[] events = event.split(" ");

        assertEquals(events.length, 3, "3 events should be fired.");
        assertEquals(events[0], "beforedomupdate", "Attribute onbeforedomupdate doesn't work");
        assertEquals(events[1], "begin", "Attribute onbegin doesn't work");
        assertEquals(events[2], "complete", "Attribute oncomplete doesn't work");
    }

    @Test
    public void testOnTypeRejected() {
        String varName = "reject";
        String testData = "RichFaces 4";
        String acceptable = "txt";
        String cmd = varName + " = " + ap + testData + ap;

        fileUploadAttributes.set(FileUploadAttributes.ontyperejected, cmd);
        fileUploadAttributes.set(FileUploadAttributes.acceptedTypes, acceptable);

        executeJS(varName + " = " + ap + ap);

        sendFileToInput(notAcceptableFile, false);

        String data = (String) executeJS("return window." + varName);
        assertEquals(data, testData);
    }

    @Test
    @Templates(exclude = { "richExtendedDataTable", "richCollapsibleSubTable" })
    public void testExecute() {
        String cmd = "executeChecker";
        final String expectedValue = "* executeChecker";

        fileUploadAttributes.set(FileUploadAttributes.execute, cmd);

        sendFile(filenames[0], true, true);

        new WebDriverWait(driver).failWith("Attribute execute does not work.").
                until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver f) {
                List<WebElement> list = page.phases;
                for (WebElement webElement : list) {
                    if (webElement.getText().equals(expectedValue)) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Test(groups = "4.Future")
    @Templates("richExtendedDataTable")
    @IssueTracking("https://issues.jboss.org/browse/RF-12122")
    public void testExecuteInEDT() {
        testExecute();
    }

    @Test(groups = "4.Future")
    @Templates("richCollapsibleSubTable")
    @IssueTracking("https://issues.jboss.org/browse/RF-12122")
    public void testExecuteInCST() {
        testExecute();
    }

    @Test
    @Templates(exclude = { "richExtendedDataTable", "richCollapsibleSubTable" })
    public void testLimitRender() {
        fileUploadAttributes.set(FileUploadAttributes.render, "renderChecker");
        fileUploadAttributes.set(FileUploadAttributes.limitRender, Boolean.TRUE);

        String statusCheckerTime = page.statusCheckerOutput.getText();
        String renderCheckerTime = page.renderCheckerOutput.getText();

        sendFile(filenames[0], true, true);

        String statusCheckerTime2 = page.statusCheckerOutput.getText();
        String renderCheckerTime2 = page.renderCheckerOutput.getText();

        assertEquals(statusCheckerTime, statusCheckerTime2);
        assertNotEquals(renderCheckerTime, renderCheckerTime2);
    }

    @Test(groups = "4.Future")
    @Templates("richExtendedDataTable")
    @IssueTracking("https://issues.jboss.org/browse/RF-12122")
    public void testLimitRenderInEDT() {
        testLimitRender();
    }

    @Test(groups = "4.Future")
    @Templates("richCollapsibleSubTable")
    @IssueTracking("https://issues.jboss.org/browse/RF-12122")
    public void testLimitRenderInCST() {
        testLimitRender();
    }

    @Test
    public void testStatus() {
        String cmd = ap + "statusChecker" + ap;
        fileUploadAttributes.set(FileUploadAttributes.status, cmd);

        String statusCheckerTime1 = page.statusCheckerOutput.getText();

        sendFile(filenames[0], true, true);

        String statusCheckerTime2 = page.statusCheckerOutput.getText();

        assertEquals(statusCheckerTime1, statusCheckerTime2);
    }

    @Test
    public void testSizeExceededLabel() {
        String testData = "size exceeded";
        fileUploadAttributes.set(FileUploadAttributes.sizeExceededLabel, testData);

        sendFile(bigFile, true, false);

        assertEquals(testData, page.uploadStatusLabel.getText(),
                "Attribute sizeExceededLabel does not work.");
    }

    @Test
    public void testMaxFilesQuantity() {
        final int maxFilesQuantity = 1;
        fileUploadAttributes.set(FileUploadAttributes.maxFilesQuantity, maxFilesQuantity);
        sendFileToInput(filenames[0], true);

        new WebDriverWait(driver, 2).failWith("File input field should not be there.").
                until(ElementNotDisplayed.getInstance().element(driver.findElement(By.cssSelector("span.rf-fu-btn-add"))));

        List<WebElement> filesToUpload = page.itemsToUpload;
        assertTrue(filesToUpload.size() == maxFilesQuantity, "More files to upload than maxFilesQuantity");
        for (int i = 0; i < maxFilesQuantity; i++) {
            String x = filesToUpload.get(i).getText();
            assertTrue(x.equals(filenames[i]), "Added file does not appear in files to upload list.");
        }

        page.uploadButton.click();

        waitUntilUploadedFilesListShow(maxFilesQuantity);
        List<WebElement> uploadedFiles = page.uploadedFilesList;
        assertTrue(uploadedFiles.size() == maxFilesQuantity, "Uploaded more files than was maxFilesQuantity. Was: "+uploadedFiles);
        for (int i = 0; i < maxFilesQuantity; i++) {
            assertTrue(uploadedFiles.get(i).getText().equals(filenames[i]),
                    "Uploaded file does not appear in uploadedList.");
        }
    }
}
