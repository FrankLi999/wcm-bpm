package org.bpwizard.bpm.boot.test.helper;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = {TestApplication.class})
@ExtendWith(ProcessEngineExtensionWithMock.class)
public class ProcessEngineWithMockTestCase {

}
