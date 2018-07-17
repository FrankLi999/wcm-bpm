package com.bpwizard.myresources.webdriver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;
import com.bpwizard.myresources.webdriver.*; 
/**
 * @author Greg Turnquist
 */
// tag::1[]
public class WebDriverAutoConfigurationTests {

	private AnnotationConfigApplicationContext context;

	@After
	public void close() {
//		if (this.context != null) {
//			this.context.close();
//		}
	}

	private void load(Class<?>[] configs, String... environment) {
//		AnnotationConfigApplicationContext applicationContext =
//			new AnnotationConfigApplicationContext();
//		applicationContext
//			.register(WebDriverAutoConfiguration.class);
//		if (configs.length > 0) {
//			applicationContext.register(configs);
//		}
//		TestPropertyValues.of(environment)
//			.applyTo(applicationContext);
//		applicationContext.refresh();
//		this.context = applicationContext;
	}
	// end::1[]

	// tag::2[]
	@Test
	public void fallbackToNonGuiModeWhenAllBrowsersDisabled() {
//		load(new Class[]{},
//			"com.bpwizard.myresources.webdriver.firefox.enabled:false",
//			"com.bpwizard.myresources.webdriver.safari.enabled:false",
//			"com.bpwizard.myresources.webdriver.chrome.enabled:false");
//
//		WebDriver driver = context.getBean(WebDriver.class);
//		assertThat(ClassUtils.isAssignable(TakesScreenshot.class,
//			driver.getClass())).isFalse();
//		assertThat(ClassUtils.isAssignable(HtmlUnitDriver.class,
//			driver.getClass())).isTrue();
	}
	// end::2[]

	// tag::3[]
	@Test
	public void testWithMockedFirefox() {
//		load(new Class[]{MockFirefoxConfiguration.class},
//			"com.bpwizard.myresources.webdriver.safari.enabled:false",
//			"com.bpwizard.myresources.webdriver.chrome.enabled:false");
//		WebDriver driver = context.getBean(WebDriver.class);
//		assertThat(ClassUtils.isAssignable(TakesScreenshot.class,
//			driver.getClass())).isTrue();
//		assertThat(ClassUtils.isAssignable(FirefoxDriver.class,
//			driver.getClass())).isTrue();
	}
	// end::3[]

	@Test
	public void testWithMockedSafari() {
//		load(new Class[]{MockSafariConfiguration.class},
//			"com.bpwizard.myresources.webdriver.firefox.enabled:false",
//			"com.bpwizard.myresources.webdriver.chrome.enabled:false");
//		WebDriver driver = context.getBean(WebDriver.class);
//		assertThat(ClassUtils.isAssignable(TakesScreenshot.class,
//			driver.getClass())).isTrue();
//		assertThat(ClassUtils.isAssignable(SafariDriver.class,
//			driver.getClass())).isTrue();
	}

	@Test
	public void testWithMockedChrome() {
//		load(new Class[]{},
//			"com.bpwizard.myresources.webdriver.firefox.enabled:false",
//			"com.bpwizard.myresources.webdriver.safari.enabled:false");
//		WebDriver driver = context.getBean(WebDriver.class);
//		assertThat(ClassUtils.isAssignable(TakesScreenshot.class,
//			driver.getClass())).isTrue();
//		assertThat(ClassUtils.isAssignable(ChromeDriver.class,
//			driver.getClass())).isTrue();
	}

	// tag::5[]
	@Configuration
	protected static class MockFirefoxConfiguration {
		@Bean
		FirefoxDriverFactory firefoxDriverFactory() {
			FirefoxDriverFactory factory =
				mock(FirefoxDriverFactory.class);
			given(factory.getObject())
				.willReturn(mock(FirefoxDriver.class));
			return factory;
		}
	}
	// end::5[]

	@Configuration
	protected static class MockSafariConfiguration {
		@Bean
		SafariDriverFactory safariDriverFactory() {
			SafariDriverFactory factory = mock(SafariDriverFactory.class);
			given(factory.getObject()).willReturn(mock(SafariDriver.class));
			return factory;
		}
	}

	@Configuration
	protected static class MockChromeConfiguration {
		@Bean
		ChromeDriverFactory chromeDriverFactory() {
			ChromeDriverFactory factory = mock(ChromeDriverFactory.class);
			given(factory.getObject()).willReturn(mock(ChromeDriver.class));
			return factory;
		}
	}

}
