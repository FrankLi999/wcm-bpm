/*
 * Public API Surface of rest-client
 */

export * from './lib/rest-client/rest-client';
export { Client } from './lib/rest-client/decorators/client';
export { Headers } from './lib/rest-client/decorators/headers';
export { Map } from './lib/rest-client/decorators/map';
export { Timeout } from './lib/rest-client/decorators/timeout';
export { OnEmit } from './lib/rest-client/decorators/on-emit';
export { Body, Header, Query, Path, PlainBody } from './lib/rest-client/decorators/parameters';
export { MediaType, Produces } from './lib/rest-client/decorators/produces';
export { Get, Post, Patch, Put, Delete, Head } from './lib/rest-client/decorators/request-methods';

export * from './lib/rest-client/rest-client.module';
export * from './lib/rest-client/rest-client-config.module';
export * from './lib/rest-client/types/api-config';
export * from './lib/rest-client/services/api-config.service';