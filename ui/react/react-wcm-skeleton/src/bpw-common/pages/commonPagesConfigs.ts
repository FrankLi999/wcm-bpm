import Error404PageConfig from './errors/404/Error404PageConfig';
import Error500PageConfig from './errors/500/Error500PageConfig';
import ClassicSearchPageConfig from './search/classic/ClassicSearchPageConfig';
import ModernSearchPageConfig from './search/modern/ModernSearchPageConfig';

const authPagesConfigs = [Error404PageConfig, Error500PageConfig, ClassicSearchPageConfig, ModernSearchPageConfig];

export default authPagesConfigs;
