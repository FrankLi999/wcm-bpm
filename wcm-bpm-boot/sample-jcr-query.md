SELECT * FROM [bpw:content] as content JOIN [bpw:contentElementFolder] as element ON 
ISDESCENDANTNODE(element, content) JOIN [bpw:propertyElementFolder] as property ON 
ISDESCENDANTNODE(property, content) WHERE content.[bpw:authoringTemplate] = '/bpwizard/library/design/authoringTemplate/MyContent'

--------------------------------

[property.jcr:primaryType,
property.jcr:mixinTypes,
property.jcr:created,
property.jcr:createdBy,
property.mode:localName,
property.mode:id,
property.mode:depth,
property.jcr:score,
property.jcr:path,
property.jcr:name,

content.bpw:authoringTemplate,
content.bpw:categories,
content.bpw:publishDate,
content.bpw:expireDate,
content.bpw:workflow,
content.bpw:workflowStage,
content.bpw:title,
content.bpw:description,
content.bpw:lifecyclePolicy,
content.bpw:currentLifecycleState,
content.jcr:lastModified,
content.jcr:lastModifiedBy,
content.jcr:versionHistory,
content.jcr:baseVersion,
content.jcr:predecessors,
content.jcr:mergeFailed,
content.jcr:activity,
content.jcr:configuration,
content.jcr:lockOwner,
content.jcr:lockIsDeep,
content.jcr:uuid,
content.jcr:primaryType,
content.jcr:mixinTypes,
content.jcr:created,
content.jcr:createdBy,
content.jcr:isCheckedOut,
content.mode:localName,
content.mode:id,
content.mode:depth,
content.jcr:score,
content.jcr:path,
content.jcr:name,

element.jcr:primaryType,
element.jcr:mixinTypes,
element.jcr:created,
element.jcr:createdBy,
element.mode:localName,
element.mode:id,
element.mode:depth,
element.jcr:score,
element.jcr:path,
element.jcr:name]
----------------------------------------
SELECT * FROM [bpw:content] as content JOIN [bpw:contentElement] as element ON ISDESCENDANTNODE(element, content) 
WHERE content.[bpw:authoringTemplate] = '/bpwizard/library/design/authoringTemplate/MyContent'

[
  content.bpw:authoringTemplate,
  content.bpw:categories,
  content.bpw:publishDate,
  content.bpw:expireDate,
  content.bpw:workflow,
  content.bpw:workflowStage,
  content.bpw:title,
  content.bpw:description,
  content.bpw:lifecyclePolicy,
  content.bpw:currentLifecycleState,
  content.jcr:lastModified,
  content.jcr:lastModifiedBy,
  content.jcr:versionHistory,
  content.jcr:baseVersion,
  content.jcr:predecessors,
  content.jcr:mergeFailed,
  content.jcr:activity,
  content.jcr:configuration,
  content.jcr:lockOwner,
  content.jcr:lockIsDeep,
  content.jcr:uuid,
  content.jcr:primaryType,
  content.jcr:mixinTypes,
  content.jcr:created,
  content.jcr:createdBy,
  content.jcr:isCheckedOut,
  content.mode:localName,
  content.mode:id,
  content.mode:depth,
  content.jcr:score,
  content.jcr:path,
  content.jcr:name,

  element.bpw:multiple,
  element.bpw:value,
  element.jcr:primaryType,
  element.jcr:mixinTypes,
  element.jcr:created,
  element.jcr:createdBy,
  element.mode:localName,
  element.mode:id,
  element.mode:depth,
  element.jcr:score,
  element.jcr:path,
  element.jcr:name]
===============================================

==================================================
element.jcr:path /{}bpwizard/{}library/{}camunda/{}rootSiteArea/{}home/{}MyContent/{}elements/{}body
element.jcr:name{}body
element.bpw:valueContent Body
content.jcr:name{}MyContent
content.jcr:path/{}bpwizard/{}library/{}camunda/{}rootSiteArea/{}home/{}MyContent
content.jcr:score1.0
element.jcr:score1.0
==================================================
==================================================
element.jcr:path/{}bpwizard/{}library/{}camunda/{}rootSiteArea/{}home/{}MyContent/{}properties/{}workflow
element.jcr:name{}workflow
element.bpw:valuebpmn:wcm_content_flow
content.jcr:name{}MyContent
content.jcr:path/{}bpwizard/{}library/{}camunda/{}rootSiteArea/{}home/{}MyContent
content.jcr:score1.0
element.jcr:score1.0
==================================================
