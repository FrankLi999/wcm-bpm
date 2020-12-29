import { CollectionViewer, DataSource } from "@angular/cdk/collections";
import { BehaviorSubject, Subscription, Observable, of } from "rxjs";
import { catchError } from "rxjs/operators";
import { AclService, WcmConstants } from "bpw-wcm-service";

export class GroupDataSource extends DataSource<String> {
  private lastPage = 0;

  private _cachedGroups = [];
  private dataStream = new BehaviorSubject<(string | undefined)[]>(
    this._cachedGroups
  );
  private subscription = new Subscription();

  constructor(private aclService: AclService, private pageSize) {
    super();
    this.loadGroups();
  }

  connect(collectionViewer: CollectionViewer): Observable<string[]> {
    this.subscription.add(
      collectionViewer.viewChange.subscribe(range => {
        const currentPage = this._getPageForIndex(range.end);
        if (currentPage > this.lastPage) {
          this.lastPage = currentPage;
          this.loadGroups();
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
    return this._cachedGroups;
  }

  loadGroups() {
    this.aclService
      .getGroups(
        WcmConstants.FILTER_NONE,
        WcmConstants.SORT_ASC,
        this.lastPage,
        this.pageSize
      )
      .pipe(catchError(() => of([])))
      .subscribe((groups: string[]) => {
        if (groups) {
          this._cachedGroups = this._cachedGroups.concat(groups);
          this.dataStream.next(this._cachedGroups);
        }
      });
  }

  private _getPageForIndex(i: number): number {
    return Math.floor(i / this.pageSize);
  }
}
