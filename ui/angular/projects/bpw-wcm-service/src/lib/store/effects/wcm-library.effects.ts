import { Injectable } from "@angular/core";
import { Actions, Effect, ofType } from "@ngrx/effects";
import { of, Observable } from "rxjs";
import { switchMap, map, catchError, filter } from "rxjs/operators";
import { LibraryService } from "../../service/library.service";
import { Library } from "../../model/Library";

import {
  WcmLibraryActionTypes,
  WcmLibraryActions,
  CreateLibrary,
  UpdateLibrary,
  LoadLibrary,
  DeleteLibrary,
  LibraryActionSuccess,
  LibraryActionFailed,
} from "../actions/wcm-library.actions";

@Injectable()
export class WcmLibraryEffects {
  constructor(
    private actions$: Actions<WcmLibraryActions>,
    private libraryService: LibraryService
  ) {}

  @Effect()
  loadLibrary$: Observable<WcmLibraryActions> = this.actions$.pipe(
    ofType<LoadLibrary>(WcmLibraryActionTypes.LOAD_LIBRARY),
    switchMap((action) => {
      return this.libraryService
        .getLibraries(
          action.payload.repository,
          action.payload.workspace,
          action.payload.filter,
          action.payload.sortDirection,
          action.payload.pageIndex,
          action.payload.pageSize
        )
        .pipe(
          filter((resp) => !!resp),
          map((response: Library[]) => {
            if (response) {
              return new LibraryActionSuccess({
                action: WcmLibraryActionTypes.LOAD_LIBRARY,
                libraries: response,
              });
            }
          }),
          catchError((err) => {
            return of(
              new LibraryActionFailed({
                action: WcmLibraryActionTypes.LOAD_LIBRARY,
                status: err,
              })
            );
          })
        );
    })
  );
  @Effect()
  createLibrary$: Observable<WcmLibraryActions> = this.actions$.pipe(
    ofType<CreateLibrary>(WcmLibraryActionTypes.CREATE_LIBRARY),
    switchMap((action) => {
      return this.libraryService.createLibrary(action.payload).pipe(
        // filter((resp) => resp != undefined),
        map((response: any) => {
          return new LibraryActionSuccess({
            action: WcmLibraryActionTypes.CREATE_LIBRARY,
            libraries: [action.payload],
          });
        }),
        catchError((err) => {
          return of(
            new LibraryActionFailed({
              action: WcmLibraryActionTypes.LOAD_LIBRARY,
              status: err,
            })
          );
        })
      );
    })
  );
  @Effect()
  updateLibrary$: Observable<WcmLibraryActions> = this.actions$.pipe(
    ofType<UpdateLibrary>(WcmLibraryActionTypes.UPDATE_LIBRARY),
    switchMap((action) => {
      return this.libraryService.saveLibrary(action.payload).pipe(
        map((response: any) => {
          return new LibraryActionSuccess({
            action: WcmLibraryActionTypes.UPDATE_LIBRARY,
            libraries: [action.payload],
          });
        }),
        catchError((err) => {
          return of(
            new LibraryActionFailed({
              action: WcmLibraryActionTypes.UPDATE_LIBRARY,
              status: err,
            })
          );
        })
      );
    })
  );
  @Effect()
  deleteLibrary$: Observable<WcmLibraryActions> = this.actions$.pipe(
    ofType<DeleteLibrary>(WcmLibraryActionTypes.DELETE_LIBRARY),
    switchMap((action) => {
      return this.libraryService.deleteLibrary(action.payload).pipe(
        map((response: any) => {
          return new LibraryActionSuccess({
            action: WcmLibraryActionTypes.DELETE_LIBRARY,
            libraries: [action.payload],
          });
        }),
        catchError((err) => {
          return of(
            new LibraryActionFailed({
              action: WcmLibraryActionTypes.DELETE_LIBRARY,
              status: err,
            })
          );
        })
      );
    })
  );
}
