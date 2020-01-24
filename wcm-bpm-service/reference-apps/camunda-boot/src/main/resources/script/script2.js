var system = java.lang.System;
system.out.println('>>>>>>>>>>>>>>>>>> script task 2');

system.out.println('>>>>>>>>>>>>>>>>>> script task 2' + collection);
system.out.println('>>>>>>>>>>>>>>>>>> script task 2' + collectionElem);

var elem2 = execution.getVariable('collectionElem')
system.out.println('>>>>>>>>>>>>>>>>>> script task 2, elem2:' + elem2);
// set process variable
execution.setVariable('task2' + loopCounter, elem2)


//throw new org.camunda.bpm.engine.delegate.BpmnError(“SignatureError”);