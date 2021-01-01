import axios from 'axios';
import { filter, map } from 'rxjs/operators';
import { from } from 'rxjs';
class ContentItemService {
  getContentItem = (repository, workspace, contentPath) => {
    //    //apiConfigService.baseUrl['auth-service']
    return from(axios.get(`/wcm/api/contentItem/get/${repository}/${workspace}?path=${contentPath}`)).pipe(
      map((resp) => resp.data),
      filter((contentItem) => !!contentItem)
    );
  };
}

const instance = new ContentItemService();

export default instance;
