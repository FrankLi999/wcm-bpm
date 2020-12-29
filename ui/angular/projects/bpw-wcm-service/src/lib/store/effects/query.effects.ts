import { Injectable } from "@angular/core";
import { Actions, Effect, ofType } from "@ngrx/effects";
import { of, Observable } from "rxjs";
import { switchMap, map, catchError, filter } from "rxjs/operators";
import { QueryStatementService } from "../../service/query-statement.service";
import { WcmUtils } from "../../utils/wcm-utils";
import { QueryStatement } from "../../model/QueryStatement";

import {
  QueryActionTypes,
  QueryActions,
  CreateQuery,
  UpdateQuery,
  LoadQuery,
  LoadQuerySuccessful,
  LoadQueryFailed,
  DeleteQuery,
  CreateQuerySuccessful,
  UpdateQuerySuccessful,
  QueryActionFailed,
  DeleteQuerySuccessful,
} from "../actions/query.actions";

@Injectable()
export class QueryEffects {
  constructor(
    private actions$: Actions<QueryActions>,
    private queryStatementService: QueryStatementService
  ) {}

  @Effect()
  loadQuery$: Observable<QueryActions> = this.actions$.pipe(
    ofType<LoadQuery>(QueryActionTypes.LOAD_QUERY),
    switchMap((action) => {
      return this.queryStatementService
        .loadQueryStatements(
          action.payload.repository,
          action.payload.workspace,
          action.payload.filter,
          action.payload.sortDirection,
          action.payload.pageIndex,
          action.payload.pageSize
        )
        .pipe(
          filter((resp) => resp != null),
          map((queries: QueryStatement[]) => {
            return new LoadQuerySuccessful(queries);
          }),
          catchError((err) => {
            return of(new LoadQueryFailed(err));
          })
        );
    })
  );
  @Effect()
  createQuery$: Observable<QueryActions> = this.actions$.pipe(
    ofType<CreateQuery>(QueryActionTypes.CREATE_QUERY),
    switchMap((action) => {
      return this.queryStatementService
        .createQueryStatement(action.payload)
        .pipe(
          filter((resp) => resp !== undefined),
          map((response: any) => {
            return new CreateQuerySuccessful(action.payload);
          }),
          catchError((err) => {
            return of(new QueryActionFailed(err));
          })
        );
    })
  );
  @Effect()
  updateQuery$: Observable<QueryActions> = this.actions$.pipe(
    ofType<UpdateQuery>(QueryActionTypes.UPDATE_QUERY),
    switchMap((action) => {
      return this.queryStatementService.saveQueryStatement(action.payload).pipe(
        filter((resp) => resp !== undefined),
        map((response: any) => {
          return new UpdateQuerySuccessful(action.payload);
        }),
        catchError((err) => {
          return of(new QueryActionFailed(err));
        })
      );
    })
  );
  @Effect()
  deleteQuery$: Observable<QueryActions> = this.actions$.pipe(
    ofType<DeleteQuery>(QueryActionTypes.DELETE_QUERY),
    switchMap((action) => {
      return this.queryStatementService
        .purgeQuery(
          action.payload.repository,
          action.payload.workspace,
          action.payload.wcmPath
        )
        .pipe(
          filter((resp) => resp !== undefined),
          map((response: any) => {
            return new DeleteQuerySuccessful({
              repository: action.payload.repository,
              workspace: action.payload.workspace,
              library: WcmUtils.library(action.payload.wcmPath),
              name: WcmUtils.itemName(action.payload.wcmPath),
            });
          }),
          catchError((err) => {
            return of(new QueryActionFailed(err));
          })
        );
    })
  );
}
