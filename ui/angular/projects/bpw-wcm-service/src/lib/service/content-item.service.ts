import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

import {
  RestClient,
  Client,
  Delete,
  Get,
  Post,
  Put,
  Body,
  Path,
  Timeout,
  Produces,
  Query,
  MediaType,
  ApiConfigService,
} from "bpw-rest-client";
import { ContentItem } from "../model/ContentItem";
import { DraftItem } from "../model/DraftItem";
import { CancelDraftRequest } from "../model/CancelDraftRequest";
import { ClaimReviewTaskRequest } from "../model/ClaimReviewTaskRequest";
import { ClaimEditTaskRequest } from "../model/ClaimEditTaskRequest";
import { EditAsDraftRequest } from "../model/EditAsDraftRequest";
import { DraftItemRequest } from "../model/DraftItemRequest";
@Injectable({
  providedIn: "root",
})
@Client({
  serviceId: "wcm",
  baseUrl: "/wcm/api/contentItem",
  headers: {
    "content-type": "application/json",
  },
})
export class ContentItemService extends RestClient {
  constructor(apiConfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiConfigService);
  }

  @Post("/create-publish")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public createAndPublishContentItem(
    @Body contentItem: ContentItem
  ): Observable<any> {
    return null;
  }

  @Put("/update-published")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public updateContentItem(@Body contentItem: ContentItem): Observable<any> {
    return null;
  }

  @Get("/get/{repository}/{workspace}")
  @Timeout(20000) //In milliseconds
  @Produces(MediaType.JSON)
  public getContentItem(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") contentItemPath: string
    //@Path('contentItemPath') ontentItemPath: string
  ): Observable<ContentItem> {
    return null;
  }

  @Put("/lock/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public lockContentItem(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") contentItemPath: string
    //@Path('contentItemPath') ontentItemPath: string
  ): Observable<ContentItem> {
    return null;
  }

  @Put("/workflow/{repository}/{workspace}/{state}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public updateWcmItemWorkflowStage(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Path("state") state: string,
    @Query("path") absPath: string
  ): Observable<any> {
    return null;
  }

  @Post("/create-draft")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public createDraft(@Body contentItem: ContentItem): Observable<any> {
    return null;
  }

  @Get("/get-draft/{repository}")
  @Timeout(20000) //In milliseconds
  @Produces(MediaType.JSON)
  public getDraftItems(
    @Path("repository") repository: string,
    @Query("path") saPath: string
    //@Path('contentItemPath') ontentItemPath: string
  ): Observable<DraftItem[]> {
    return null;
  }

  @Post("/update-draft")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public updateDraft(@Body contentItem: ContentItem): Observable<any> {
    return null;
  }

  @Post("/cancel-draft")
  @Timeout(20000) //In milliseconds
  @Produces(MediaType.JSON)
  public cancelDraft(@Body draftRequest: CancelDraftRequest): Observable<any> {
    return null;
  }

  @Post("/claim-review-task")
  @Timeout(20000) //In milliseconds
  @Produces(MediaType.JSON)
  public claimReviewTask(
    @Body claimRequest: ClaimReviewTaskRequest
  ): Observable<string> {
    return null;
  }

  @Post("/claim-edit-task")
  @Timeout(20000) //In milliseconds
  @Produces(MediaType.JSON)
  public claimEditTask(
    @Body claimRequest: ClaimEditTaskRequest
  ): Observable<string> {
    return null;
  }

  @Post("/approve-draft")
  @Timeout(20000) //In milliseconds
  @Produces(MediaType.JSON)
  public approveDraft(
    @Body approveRequest: DraftItemRequest
  ): Observable<string> {
    return null;
  }

  @Post("/reject-draft")
  @Timeout(20000) //In milliseconds
  @Produces(MediaType.JSON)
  public rejectDraft(
    @Body rejectRequest: DraftItemRequest
  ): Observable<string> {
    return null;
  }

  @Post("/edit-as-draft")
  @Timeout(20000) //In milliseconds
  @Produces(MediaType.JSON)
  public editAsDraft(
    @Body draftRequest: EditAsDraftRequest
  ): Observable<string> {
    return null;
  }

  @Delete("/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public purgeContentItem(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") absPath: string
  ): Observable<any> {
    return null;
  }
}
