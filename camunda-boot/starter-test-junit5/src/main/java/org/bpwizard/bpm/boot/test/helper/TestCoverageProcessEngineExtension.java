package org.bpwizard.bpm.boot.test.helper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.event.EventHandler;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.CoverageTestRunState;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.MinimalCoverageMatcher;
import org.camunda.bpm.extension.process_test_coverage.listeners.CompensationEventCoverageHandler;
import org.camunda.bpm.extension.process_test_coverage.listeners.FlowNodeHistoryEventHandler;
import org.camunda.bpm.extension.process_test_coverage.listeners.PathCoverageParseListener;
import org.camunda.bpm.extension.process_test_coverage.model.AggregatedCoverage;
import org.camunda.bpm.extension.process_test_coverage.model.ClassCoverage;
import org.camunda.bpm.extension.process_test_coverage.model.MethodCoverage;
import org.camunda.bpm.extension.process_test_coverage.util.CoverageReportUtil;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtensionContext;

public class TestCoverageProcessEngineExtension extends ProcessEngineExtension {
	 /**
     * The state of the current run (class and current method).
     */
    private CoverageTestRunState coverageTestRunState;
    public static final String DEFAULT_ASSERT_AT_LEAST_PROPERTY = "org.camunda.bpm.extension.process_test_coverage.ASSERT_AT_LEAST";
    /**
     * Controls run state initialization.
     * {@see #initializeRunState(Description)}
     */
    private boolean firstRun = true;

    /**
     * Log class and test method coverages?
     */
    // private boolean detailedCoverageLogging = false;

    /**
     * Matchers to be asserted on the class coverage percentage.
     */
    private Collection<Matcher<Double>> classCoverageAssertionMatchers = new LinkedList<Matcher<Double>>();

    /**
     * Matchers to be asserted on the individual test method coverages.
     */
    private Map<String, Collection<Matcher<Double>>> testMethodNameToCoverageMatchers = new HashMap<String, Collection<Matcher<Double>>>();

    /**
     * A list of process definition keys excluded from the test run.
     */
    private List<String> excludedProcessDefinitionKeys;
    
    public TestCoverageProcessEngineExtension() {
        super();
        this.addMatcherRule(DEFAULT_ASSERT_AT_LEAST_PROPERTY);
    }

    public TestCoverageProcessEngineExtension(final ProcessEngine processEngine) {
        super(processEngine);
        this.addMatcherRule(DEFAULT_ASSERT_AT_LEAST_PROPERTY);
    }
    
    @Override
    public void beforeTestExecution(final ExtensionContext extensionContext) {
    	// validateRuleAnnotations(extensionContext);

//        if (processEngine == null) {
//            super.initializeProcessEngine();
//        }

        initializeRunState(extensionContext);

        super.beforeTestExecution(extensionContext);

        initializeMethodCoverage(extensionContext);
    }
    
    @Override
    public void afterTestExecution(final ExtensionContext extensionContext) {
    	handleTestMethodCoverage(extensionContext);

        handleClassCoverage(extensionContext);

        // run derived finalization only of not used as a class rule
        if (this.getThreadLocalProcessEngine().getIdentityService() != null) {
            super.afterTestExecution(extensionContext);
        }
    }
    
    /**
     * Logs and asserts the test method coverage and creates a graphical report.
     * 
     * @param description
     */
    private void handleTestMethodCoverage(final ExtensionContext extensionContext) {

        // Do test method coverage only if deployments present
        final Deployment methodDeploymentAnnotation = extensionContext.getElement().get().getAnnotation(Deployment.class);
        final Deployment classDeploymentAnnotation = extensionContext.getTestClass().get().getAnnotation(Deployment.class);
        final boolean testMethodHasDeployment = methodDeploymentAnnotation != null || classDeploymentAnnotation != null;

        if (testMethodHasDeployment) {
        	final ProcessEngine processEngine = this.getThreadLocalProcessEngine();
            final String testName = this.getMethodName(extensionContext);
            final MethodCoverage testCoverage = coverageTestRunState.getTestMethodCoverage(testName);

            double coveragePercentage = testCoverage.getCoveragePercentage();

            // Log coverage percentage
            // logger.info(testName + " test method coverage is " + coveragePercentage);

            logCoverageDetail(testCoverage);

            // Create graphical report
            CoverageReportUtil.createCurrentTestMethodReport(processEngine, coverageTestRunState);

            if (testMethodNameToCoverageMatchers.containsKey(testName)) {

                assertCoverage(coveragePercentage, testMethodNameToCoverageMatchers.get(testName));

            }

        }
    }

    
    /**
     * If the rule is a @ClassRule log and assert the coverage and create a
     * graphical report. For the class coverage to work all the test method
     * deployments have to be equal.
     * 
     * @param description
     */
    private void handleClassCoverage(final ExtensionContext extensionContext) {

        // If the rule is a class rule get the class coverage
        // if (!description.isTest()) {
    	if (extensionContext.getTestMethod().isEmpty()) {
    		final ProcessEngine processEngine = this.getThreadLocalProcessEngine();

            final ClassCoverage classCoverage = coverageTestRunState.getClassCoverage();

            // Make sure the class coverage deals with the same deployments for
            // every test method
            classCoverage.assertAllDeploymentsEqual();

            final double classCoveragePercentage = classCoverage.getCoveragePercentage();

            // Log coverage percentage
//            logger.info(
//                    coverageTestRunState.getTestClassName() + " test class coverage is: " + classCoveragePercentage);

            logCoverageDetail(classCoverage);

            // Create graphical report
            CoverageReportUtil.createClassReport(processEngine, coverageTestRunState);

            assertCoverage(classCoveragePercentage, classCoverageAssertionMatchers);

        }
    }
    
    
    /**
     * Logs the string representation of the passed coverage object.
     * 
     * @param coverage
     */
    private void logCoverageDetail(AggregatedCoverage coverage) {

//        if (logger.isLoggable(Level.FINE) || isDetailedCoverageLogging()) {
//            logger.log(Level.INFO, coverage.toString());
//        }

    }
    
    private void assertCoverage(double coverage, Collection<Matcher<Double>> matchers) {

        for (Matcher<Double> matcher : matchers) {

            // Assert.assertThat(coverage, matcher);
            Assertions.assertTrue(matcher.matches(coverage));
        }

    }
    
    /**
     * Validates the annotation of the rule field in the test class.
     * 
     * @param description
     */
//    private void validateRuleAnnotations(final ExtensionContext extensionContext) {
//
//        // If the first run is a @ClassRule run, check if @Rule is annotated
//    	
//        if (firstRun && !description.isTest()) {
//
//            /*
//             * Get the fields of the test class and check if there is only one
//             * coverage rule and if the coverage rule field is annotation with
//             * both @ClassRule and @Rule.
//             */
//
//            int numberOfCoverageRules = 0;
//            for (Field field : extensionContext.getTestClass().get().getFields()) {
//
//                final Class<?> fieldType = field.getType();
//                if (getClass().isAssignableFrom(fieldType)) {
//
//                    ++numberOfCoverageRules;
//
//                    final boolean isClassRule = field.isAnnotationPresent(ClassRule.class);
//                    final boolean isRule = field.isAnnotationPresent(Rule.class);
//                    if (isClassRule && !isRule) {
//
//                        throw new RuntimeException(getClass().getCanonicalName()
//                                + " can only be used as a @ClassRule if it is also a @Rule!");
//                    }
//                }
//            }
//
//            // TODO if they really want to have multiple runs, let them?
//            if (numberOfCoverageRules > 1) {
//                throw new RuntimeException("Only one coverage rule can be used per test class!");
//            }
//        }
//    }
    
    /**
     * Initialize the coverage run state depending on the rule annotations and
     * notify the state of the current test name.
     * 
     * @param description
     */
    private void initializeRunState(final ExtensionContext extensionContext) {

        // Initialize new state once on @ClassRule run or on every individual
        // @Rule run
        if (firstRun) {

            coverageTestRunState = new CoverageTestRunState();
            coverageTestRunState.setTestClassName(this.getClassName(extensionContext));
            coverageTestRunState.setExcludedProcessDefinitionKeys(excludedProcessDefinitionKeys);

            initializeListenerRunState(extensionContext);

            firstRun = false;
        }

        coverageTestRunState.setCurrentTestMethodName(this.getMethodName(extensionContext));

    }
    
    /**
     * Sets the test run state for the coverage listeners. logging.
     * {@see ProcessCoverageInMemProcessEngineConfiguration}
     */
    private void initializeListenerRunState(final ExtensionContext extensionContext) {
    	final ProcessEngine processEngine = this.getThreadLocalProcessEngine();
        final ProcessEngineConfigurationImpl processEngineConfiguration = (ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration();

        // Configure activities listener

        final FlowNodeHistoryEventHandler historyEventHandler = (FlowNodeHistoryEventHandler) processEngineConfiguration.getHistoryEventHandler();
        historyEventHandler.setCoverageTestRunState(coverageTestRunState);

        // Configure sequence flow listener

        final List<BpmnParseListener> bpmnParseListeners = processEngineConfiguration.getCustomPostBPMNParseListeners();

        for (BpmnParseListener parseListener : bpmnParseListeners) {

            if (parseListener instanceof PathCoverageParseListener) {

                final PathCoverageParseListener listener = (PathCoverageParseListener) parseListener;
                listener.setCoverageTestRunState(coverageTestRunState);
            }
        }

        // Compensation event handler

        final EventHandler compensationEventHandler = processEngineConfiguration.getEventHandler("compensate");
        if (compensationEventHandler != null && compensationEventHandler instanceof CompensationEventCoverageHandler) {

            final CompensationEventCoverageHandler compensationEventCoverageHandler = (CompensationEventCoverageHandler) compensationEventHandler;
            compensationEventCoverageHandler.setCoverageTestRunState(coverageTestRunState);

        } else {
//            logger.warning("CompensationEventCoverageHandler not registered with process engine configuration!"
//                    + " Compensation boundary events coverage will not be registered.");
        }

    }
    
    /**
     * Initialize the current test method coverage.
     * 
     * @param description
     */
    private void initializeMethodCoverage(final ExtensionContext extensionContext) {

        // Not a @ClassRule run and deployments present
    	final String deploymentId = this.getDeploymentId();
    	final ProcessEngine processEngine = this.getThreadLocalProcessEngine();
        if (deploymentId != null) {

            final List<ProcessDefinition> deployedProcessDefinitions = processEngine.getRepositoryService().createProcessDefinitionQuery().deploymentId(
                deploymentId).list();

            final List<ProcessDefinition> relevantProcessDefinitions = new ArrayList<ProcessDefinition>();
            for (ProcessDefinition definition: deployedProcessDefinitions) {
                if (!isExcluded(definition)) {
                    relevantProcessDefinitions.add(definition);
                }
            }

            coverageTestRunState.initializeTestMethodCoverage(processEngine,
                    deploymentId,
                    relevantProcessDefinitions,
                    this.getMethodName(extensionContext));

        }
    }
    
    private boolean isExcluded(ProcessDefinition processDefinition) {
        if (excludedProcessDefinitionKeys != null) {
            return excludedProcessDefinitionKeys.contains(processDefinition.getKey());
        }
        return false;
    }
    
    private String getClassName(final ExtensionContext extensionContext) {
    	return extensionContext.getTestClass().isPresent() ? extensionContext.getTestClass().get().getName() : "";
    }
    
    private String getMethodName(final ExtensionContext extensionContext) {
    	return extensionContext.getTestMethod().isPresent() ? extensionContext.getTestMethod().get().getName() : "";
    }
    
    private void addMatcherRule(String key) {
    	String assertAtLeast = System.getProperty(key);
        if (assertAtLeast != null) {
            try {

                final MinimalCoverageMatcher minimalCoverageMatcher = new MinimalCoverageMatcher(
                        Double.parseDouble(assertAtLeast));
               addClassCoverageAssertionMatcher(minimalCoverageMatcher);

            } catch (NumberFormatException e) {
                throw new RuntimeException("BAD TEST CONFIGURATION: optionalAssertCoverageAtLeastProperty( \"" + key
                        + "\" ) must be double");
            }
        }
    }
    
    public void addClassCoverageAssertionMatcher(MinimalCoverageMatcher matcher) {
        this.classCoverageAssertionMatchers.add(matcher);
    }
}
