import ApiGatewayWelcome from '../elements/Welcome';
import FirewallSelectors from '../elements/handlers/firewall/Selectors';
import FirewallSelectorRules from '../elements/handlers/firewall/SelectorRules';
import SpringCloudSelectors from '../elements/handlers/SpringCloud/Selectors';
import SpringCloudSelectorRules from '../elements/handlers/SpringCloud/SelectorRules';
import SignSelectors from '../elements/handlers/sign/Selectors';
import SignSelectorRules from '../elements/handlers/sign/SelectorRules';
import MonitorSelectors from '../elements/handlers/monitor/Selectors';
import MonitorSelectorRules from '../elements/handlers/monitor/SelectorRules';
import DivideSelectors from '../elements/handlers/divide/Selectors';
import DivideSelectorRules from '../elements/handlers/divide/SelectorRules';
import HystrixSelectors from '../elements/handlers/hystrix/Selectors';
import HystrixSelectorRules from '../elements/handlers/hystrix/SelectorRules';
import RateLimiterSelectors from '../elements/handlers/rateLimiter/Selectors';
import RateLimiterSelectorRules from '../elements/handlers/rateLimiter/SelectorRules';
import RewriteSelectors from '../elements/handlers/rewrite/Selectors';
import RewriteSelectorRules from '../elements/handlers/rewrite/SelectorRules';
import AccountManagement from '../elements/system/account/AccountManagement';
import Metadata from '../elements/system/metadata/metadata';
import AppAuth from '../elements/system/appAuth/AppAuth';
import RequestHandlers from '../elements/system/handlers/Handlers';
const wcmElements = {
  ApiGatewayWelcome,
  FirewallSelectors,
  FirewallSelectorRules,
  SpringCloudSelectors,
  SpringCloudSelectorRules,
  SignSelectors,
  SignSelectorRules,
  MonitorSelectors,
  MonitorSelectorRules,
  DivideSelectors,
  DivideSelectorRules,
  HystrixSelectors,
  HystrixSelectorRules,
  RateLimiterSelectors,
  RateLimiterSelectorRules,
  RewriteSelectors,
  RewriteSelectorRules,
  AccountManagement,
  Metadata,
  AppAuth,
  RequestHandlers,
};
export default wcmElements;
