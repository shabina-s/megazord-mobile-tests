package com.rally.automation.megazord.common;

import com.rally.automation.framework.TestPropertyManager;
import com.rally.automation.framework.desiredcapability.DesiredCapabilityManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.json.simple.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseTest {

  private DesiredCapabilities desiredCapabilities;
  public AppiumDriver driver;

  @BeforeSuite
  public void InitProjectSetup() throws IOException {
    TestPropertyManager.initTestProperties();
    desiredCapabilities = DesiredCapabilityManager.getDesiredCapabilities();

    if (TestPropertyManager.getTestPlatform().equalsIgnoreCase("ios")) {
      List<String> processArgs = new ArrayList<>(Arrays.asList(
              "-DtestEnv=" + TestPropertyManager.getTestEnv()
      ));
      final JSONObject argsValue = new JSONObject();
      argsValue.put("args", processArgs);
      desiredCapabilities.setCapability("processArguments", argsValue);
    }
    else if (TestPropertyManager.getTestPlatform().equalsIgnoreCase("android")) {
      String envArg = "-e env " + TestPropertyManager.getTestEnv();
      desiredCapabilities.setCapability("optionalIntentArguments", envArg);
    }
  }

  @BeforeMethod
  public void InitApplication() throws MalformedURLException {
    if (TestPropertyManager.getTestPlatform().equalsIgnoreCase("ios")) {
      driver = new IOSDriver<>(new URL("http://localhost:4723/wd/hub"), desiredCapabilities);

    } else if (TestPropertyManager.getTestPlatform().equalsIgnoreCase("android")) {
      driver = new AndroidDriver<>(new URL("http://localhost:4723/wd/hub"), desiredCapabilities);
    }
  }


  @AfterMethod
  public void TearDownApplication() {
    if (driver != null) driver.quit();
  }
}
