import { CollectionViewer, DataSource } from "@angular/cdk/collections";
import { BehaviorSubject, Subscription, Observable, of } from "rxjs";
import { catchError } from "rxjs/operators";
import { AclService, WcmConstants } from "bpw-wcm-service";

export class UserDataSource extends DataSource<String> {
  private lastPage = 0;

  private _cachedUsers = [];
  private dataStream = new BehaviorSubject<(string | undefined)[]>(
    this._cachedUsers
  );
  private subscription = new Subscription();

  constructor(private aclService: AclService, private pageSize) {
    super();
    this.loadUsers();
  }

  connect(collectionViewer: CollectionViewer): Observable<string[]> {
    this.subscription.add(
      collectionViewer.viewChange.subscribe(range => {
        const currentPage = this._getPageForIndex(range.end);
        if (currentPage > this.lastPage) {
          this.lastPage = currentPage;
          this.loadUsers();
        }
      })
    );
    return this.dataStream;
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.subscription.unsubscribe();
    this.dataStream.complete();
  }

  get cachedGroups() {
    return this._cachedUsers;
  }

  loadUsers() {
    this.aclService
      .getUsers(
        WcmConstants.FILTER_NONE,
        WcmConstants.SORT_ASC,
        this.lastPage,
        this.pageSize
      )
      .pipe(catchError(() => of([])))
      .subscribe((users: string[]) => {
        if (users) {
          this._cachedUsers = this._cachedUsers.concat(users);
          this.dataStream.next(this._cachedUsers);
        }
      });
  }

  private _getPageForIndex(i: number): number {
    return Math.floor(i / this.pageSize);
  }
}
