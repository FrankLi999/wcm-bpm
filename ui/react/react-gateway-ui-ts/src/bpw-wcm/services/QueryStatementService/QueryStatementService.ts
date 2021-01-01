import axios from 'axios';
import { map, filter } from 'rxjs/operators';
import { from } from 'rxjs';
class QueryStatementService {
  // @Post("/query")

  executeQueryStatement = (queryStatement) => {
        //apiConfigService.baseUrl['auth-service']
    return from(axios.post('/wcm/api/queryStatement/query', queryStatement)).pipe(
      map((resp) => resp.data),
      filter((result) => !!result)
    );
  };
}
const instance = new QueryStatementService();
export default instance;
